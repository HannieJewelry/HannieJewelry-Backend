package hanniejewelry.vn.shipping.service;

import hanniejewelry.vn.order.application.command.MarkOrderFulfilledCommand;
import hanniejewelry.vn.order.application.command.MarkOrderPaidCommand;
import hanniejewelry.vn.order.domain.model.entity.OrderLineItem;
import hanniejewelry.vn.order.domain.model.enums.LineItemFulfillmentStatus;
import hanniejewelry.vn.order.domain.model.enums.OrderProcessingStatus;
import hanniejewelry.vn.order.entity.Order;
import hanniejewelry.vn.order.domain.model.enums.FulfillmentStatus;
import hanniejewelry.vn.order.repository.OrderRepository;
import hanniejewelry.vn.product.entity.ProductVariant;
import hanniejewelry.vn.product.repository.ProductVariantRepository;
import hanniejewelry.vn.shared.exception.BaseMessageType;
import hanniejewelry.vn.shared.exception.BizException;
import hanniejewelry.vn.shared.dto.GenericFilterRequest;
import hanniejewelry.vn.shipping.api.dto.ShipmentRequest;
import hanniejewelry.vn.shipping.api.dto.UpdateCODStatusRequest;
import hanniejewelry.vn.shipping.api.dto.UpdateShipmentStatusRequest;
import hanniejewelry.vn.shipping.domain.model.entity.Shipment;
import hanniejewelry.vn.shipping.domain.model.entity.ShipmentItem;
import hanniejewelry.vn.shipping.domain.model.enums.CODStatus;
import hanniejewelry.vn.shipping.domain.model.enums.ShipmentStatus;
import hanniejewelry.vn.shipping.domain.model.mapper.ShipmentMapper;
import hanniejewelry.vn.shipping.domain.model.repository.ShipmentRepository;
import hanniejewelry.vn.shipping.domain.model.view.ShipmentView;
import hanniejewelry.vn.shipping.domain.model.view.ShipmentView.ShipmentItemView;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.Map;
import java.util.HashMap;
import hanniejewelry.vn.shipping.api.dto.FulfillmentRequest;

@Service
@Transactional
@Slf4j
@RequiredArgsConstructor
public class ShipmentService {

    private final ShipmentRepository shipmentRepository;
    private final OrderRepository orderRepository;
    private final ProductVariantRepository productVariantRepository;
    private final CommandGateway commandGateway;
    private final ShipmentMapper shipmentMapper;

    public Page<ShipmentView> getAllShipmentsWithFilter(GenericFilterRequest filter) {
        Sort sort = Sort.by(
                (filter.getDirection() != null && "DESC".equalsIgnoreCase(filter.getDirection()))
                        ? Sort.Direction.DESC
                        : Sort.Direction.ASC,
                filter.getSortProperty() != null ? filter.getSortProperty() : "createdAt"
        );

        Integer page = filter.getPage();
        Integer size = filter.getSize();
        
        PageRequest pageRequest = PageRequest.of(page, size, sort);

        Page<Shipment> shipmentPage = shipmentRepository.findAll(pageRequest);

        List<ShipmentView> content = shipmentPage.getContent().stream()
                .map(this::mapToView)
                .collect(Collectors.toList());

        return new PageImpl<>(content, pageRequest, shipmentPage.getTotalElements());
    }

    public ShipmentView getShipmentViewById(UUID id) {
        Shipment shipment = shipmentRepository.findById(id)
                .orElseThrow(() -> new BizException(BaseMessageType.NOT_FOUND, "Shipment not found"));
        return mapToView(shipment);
    }

    public ShipmentView createShipmentAndReturnView(ShipmentRequest request) {
        Order order = orderRepository.findById(request.getOrderId())
                .orElseThrow(() -> new BizException(BaseMessageType.NOT_FOUND, "Order not found"));

        if (order.getFulfillmentStatus() != FulfillmentStatus.UNFULFILLED) {
            throw new BizException(BaseMessageType.INVALID_STATE, "Order is already fulfilled");
        }

        Shipment shipment = shipmentMapper.toEntity(request, order);

        if (order.getPaymentMethodId() != null &&
                (order.getPaymentMethodId() == 1L ||
                        (order.getPaymentMethod() != null && "cod".equalsIgnoreCase(order.getPaymentMethod())))) {
            shipment.setCodAmount(order.getTotalPrice());
            shipment.setCodStatus(CODStatus.PENDING);
            log.info("Setting COD amount for shipment: {}", order.getTotalPrice());
        } else {
            shipment.setCodAmount(BigDecimal.ZERO);
            shipment.setCodStatus(CODStatus.NOT_APPLICABLE);
        }

        order.getLineItems().forEach(lineItem -> {
            ShipmentItem item = shipmentMapper.toShipmentItem(
                    lineItem.getVariantId(), 
                    lineItem.getId(),
                    lineItem.getProductId(),
                    lineItem.getQuantity()
            );
            item.setShipment(shipment);
            shipment.getItems().add(item);
        });

        Shipment saved = shipmentRepository.save(shipment);
        shipmentRepository.flush();

        log.info("Created shipment {} for order {}", saved.getId(), order.getId());

        return mapToView(saved);
    }

    public ShipmentView updateShipmentStatus(UUID id, UpdateShipmentStatusRequest request) {
        Shipment shipment = shipmentRepository.findById(id)
                .orElseThrow(() -> new BizException(BaseMessageType.NOT_FOUND, "Shipment not found"));

        // Update shipment status
        ShipmentStatus oldStatus = shipment.getStatus();
        shipmentMapper.updateShipmentStatus(shipment, request.getStatus());

        if (request.getNote() != null && !request.getNote().isEmpty()) {
            shipment.setNotes(request.getNote());
        }

        log.info("Updating shipment {} status from {} to {}", id, oldStatus, request.getStatus());

        switch (request.getStatus()) {
            case DELIVERED:
                handleDeliveredStatus(shipment);
                break;

            case FAILED:
                log.warn("Delivery failed for shipment: {}, order: {}", 
                        shipment.getId(), shipment.getOrder().getOrderCode());
                break;

            case WAITING_FOR_RETURN:
                log.info("Shipment {} is waiting to be returned", shipment.getId());
                break;

            case RETURNED:
                handleReturnedStatus(shipment);
                break;

            case CANCELLED:
                handleCancelledStatus(shipment);
                break;

            case PICKUP_FAILED:
                log.warn("Pickup failed for shipment: {}, order: {}", 
                        shipment.getId(), shipment.getOrder().getOrderCode());
                break;

            default:
                break;
        }

        Shipment saved = shipmentRepository.save(shipment);
        shipmentRepository.flush();

        // Synchronize order fulfillment status based on all shipments
        synchronizeOrderFulfillmentStatus(saved.getOrder());

        log.info("Updated shipment {} status to {}", id, request.getStatus());

        return mapToView(saved);
    }

    public ShipmentView updateCODStatus(UUID id, UpdateCODStatusRequest request) {
        Shipment shipment = shipmentRepository.findById(id)
                .orElseThrow(() -> new BizException(BaseMessageType.NOT_FOUND, "Shipment not found"));

        CODStatus oldStatus = shipment.getCodStatus();
        shipmentMapper.updateCODStatus(shipment, request.getStatus(), request.getAmount());

        if (request.getNote() != null && !request.getNote().isEmpty()) {
            shipment.setNotes(request.getNote());
        }

        if (request.getStatus() == CODStatus.RECEIVED) {
            commandGateway.sendAndWait(new MarkOrderPaidCommand(
                    shipment.getOrder().getId(),
                    shipment.getOrder().getOrderCode(),
                    shipment.getCodAmount()
            ));

            log.info("Order {} marked as paid for COD amount {}", 
                    shipment.getOrder().getId(), shipment.getCodAmount());
        }

        Shipment saved = shipmentRepository.save(shipment);
        shipmentRepository.flush();

        log.info("Updated shipment {} COD status from {} to {}", id, oldStatus, request.getStatus());

        return mapToView(saved);
    }

    public byte[] generateShippingLabel(UUID id) {
        Shipment shipment = shipmentRepository.findById(id)
                .orElseThrow(() -> new BizException(BaseMessageType.NOT_FOUND, "Shipment not found"));

        shipment.setPrintCount(shipment.getPrintCount() + 1);
        shipment.setLastPrintedDate(Instant.now());
        shipmentRepository.save(shipment);
        shipmentRepository.flush();

        log.info("Generated shipping label for shipment {}", id);
        return "SHIPPING LABEL PLACEHOLDER".getBytes();
    }

    @Transactional
    public Map<String, Object> createFulfillmentForOrder(UUID orderId, FulfillmentRequest request) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new BizException(BaseMessageType.NOT_FOUND, "Order not found"));

        if (order.getFulfillmentStatus() != FulfillmentStatus.UNFULFILLED) {
            throw new BizException(BaseMessageType.INVALID_STATE, "Order is already fulfilled");
        }

        Long packageId = request.getPackageId();
        String packageName = request.getPackageName() != null ? request.getPackageName() : "Khác";
        
        ShipmentRequest shipmentRequest = ShipmentRequest.builder()
                .orderId(orderId)
                .carrierCode("OTHER")
                .carrierName(packageName)
                .notes(null)
                .build();

        Shipment shipment = shipmentMapper.toEntity(shipmentRequest, order);

        if (order.getPaymentMethodId() != null &&
                (order.getPaymentMethodId() == 1L ||
                        (order.getPaymentMethod() != null && "cod".equalsIgnoreCase(order.getPaymentMethod())))) {
            shipment.setCodAmount(order.getTotalPrice());
            shipment.setCodStatus(CODStatus.PENDING);
            log.info("Setting COD amount for shipment: {}", order.getTotalPrice());
        } else {
            shipment.setCodAmount(BigDecimal.ZERO);
            shipment.setCodStatus(CODStatus.NOT_APPLICABLE);
        }

        order.getLineItems().forEach(lineItem -> {
            ShipmentItem item = shipmentMapper.toShipmentItem(
                    0L,
                    lineItem.getId(),
                    lineItem.getProductId(),
                    lineItem.getQuantity()
            );
            item.setShipment(shipment);
            shipment.getItems().add(item);
        });

        Shipment saved = shipmentRepository.save(shipment);
        shipmentRepository.flush();

        log.info("Created fulfillment shipment {} for order {}", saved.getId(), order.getId());

        Map<String, Object> response = new HashMap<>();
        Map<String, Object> data = new HashMap<>();
        
        data.put("id", saved.getId());
        data.put("createdDate", saved.getCreatedAt().toString());
        data.put("orderNumber", order.getOrderNumber());
        data.put("orderId", order.getId());
        data.put("codAmount", saved.getCodAmount());
        data.put("tracking_Company", saved.getCarrierName());
        data.put("carrierStatus", 1);
        data.put("paymentStatusId", 2);
        data.put("carrierStatusName", "Chờ lấy hàng");
        data.put("carrierCODStatus", 2);
        data.put("carrierCODStatusName", "Chưa nhận");
        
        List<Map<String, Object>> shipmentProducts = new ArrayList<>();
        order.getLineItems().forEach(lineItem -> {
            Map<String, Object> product = new HashMap<>();
            product.put("orderProductId", lineItem.getId());
            product.put("quantity", lineItem.getQuantity());
            product.put("productName", lineItem.getTitle());
            product.put("price", lineItem.getPrice());
            product.put("productId", lineItem.getProductId());
            product.put("variantValue", lineItem.getVariantTitle());
            product.put("isDeleted", false);
            product.put("sku", lineItem.getSku());
            product.put("totalPrice", lineItem.getPrice().multiply(new BigDecimal(lineItem.getQuantity())));
            product.put("tracking_Number", null);
            product.put("tracking_Url", "http://www.google.com/search?q=");
            product.put("imageUrl", null);
            shipmentProducts.add(product);
        });
        data.put("shipmentProducts", shipmentProducts);
        
        data.put("shippingFirstName", order.getShippingAddress().getFirstName());
        data.put("shippingLastName", order.getShippingAddress().getLastName());
        data.put("shippingFullName", order.getShippingAddress().getFirstName() + " " + order.getShippingAddress().getLastName());
        data.put("shippingAddress", order.getShippingAddress().getAddress1());
        data.put("provinceId", order.getShippingAddress().getProvinceCode());
        data.put("provinceName", order.getShippingAddress().getProvince());
        data.put("districtId", order.getShippingAddress().getDistrictCode());
        data.put("districtName", order.getShippingAddress().getDistrict());
        data.put("wardId", order.getShippingAddress().getWardCode());
        data.put("wardName", order.getShippingAddress().getWard());
        data.put("customerId", order.getUserId());
        data.put("shippingPhone", order.getShippingAddress().getPhone());
        data.put("readyToPickDate", saved.getCreatedAt().toString());
        
        response.put("data", data);
        response.put("errors", null);
        response.put("errorCodes", null);
        
        return response;
    }

    private void handleDeliveredStatus(Shipment shipment) {
        updateInventoryOnDelivery(shipment);

        commandGateway.sendAndWait(new MarkOrderFulfilledCommand(
                shipment.getOrder().getId()
        ));

        log.info("Order {} marked as fulfilled", shipment.getOrder().getId());
    }

    private void updateInventoryOnDelivery(Shipment shipment) {
        shipment.getItems().forEach(item -> {
            Long variantId = item.getVariantId();
            if (variantId == null) {
                log.warn("Variant ID is null for shipment item");
                return;
            }

            try {
                ProductVariant variant = productVariantRepository.findById(variantId).orElse(null);
                if (variant == null) {
                    log.warn("Product variant not found for ID: {}", variantId);
                    return;
                }

                if (variant.getInventoryAdvance() == null) {
                    log.warn("Inventory data not found for variant: {}", variant.getId());
                    return;
                }

                int quantity = item.getQuantity();

                int currentQtyOnhand = variant.getInventoryAdvance().getQtyOnhand() != null ?
                        variant.getInventoryAdvance().getQtyOnhand() : 0;
                int currentQtyCommitted = variant.getInventoryAdvance().getQtyCommited() != null ? 
                        variant.getInventoryAdvance().getQtyCommited() : 0;

                log.info("Delivery completed - Updating inventory for variant {}: OnHand: {}, Committed: {}, Quantity: {}",
                        variant.getTitle(), currentQtyOnhand, currentQtyCommitted, quantity);

                variant.getInventoryAdvance().setQtyOnhand(Math.max(0, currentQtyOnhand - quantity));

                variant.getInventoryAdvance().setQtyCommited(Math.max(0, currentQtyCommitted - quantity));

                variant.setInventoryQuantity(variant.getInventoryAdvance().getQtyOnhand());

                productVariantRepository.save(variant);

                log.info("Inventory updated for variant {}: New OnHand: {}, New Committed: {}",
                        variant.getTitle(), variant.getInventoryAdvance().getQtyOnhand(), 
                        variant.getInventoryAdvance().getQtyCommited());
            } catch (Exception e) {
                log.error("Failed to update inventory for variant ID: {}", variantId, e);
            }
        });
    }

    private void handleReturnedStatus(Shipment shipment) {
        shipment.getItems().forEach(item -> {
            Long variantId = item.getVariantId();
            if (variantId == null) return;

            try {
                ProductVariant variant = productVariantRepository.findById(variantId).orElse(null);
                if (variant == null || variant.getInventoryAdvance() == null) return;

                int quantity = item.getQuantity();

                int currentQtyOnhand = variant.getInventoryAdvance().getQtyOnhand() != null ?
                        variant.getInventoryAdvance().getQtyOnhand() : 0;
                int currentQtyCommitted = variant.getInventoryAdvance().getQtyCommited() != null ? 
                        variant.getInventoryAdvance().getQtyCommited() : 0;
                int currentQtyAvailable = variant.getInventoryAdvance().getQtyAvailable() != null ? 
                        variant.getInventoryAdvance().getQtyAvailable() : 0;

                log.info("Return received - Restoring inventory for variant {}: Before: OnHand: {}, Available: {}", 
                        variant.getTitle(), currentQtyOnhand, currentQtyAvailable);

                variant.getInventoryAdvance().setQtyOnhand(currentQtyOnhand + quantity);
                variant.getInventoryAdvance().setQtyCommited(Math.max(0, currentQtyCommitted - quantity));
                variant.getInventoryAdvance().setQtyAvailable(currentQtyAvailable + quantity);

                variant.setInventoryQuantity(variant.getInventoryAdvance().getQtyOnhand());

                productVariantRepository.save(variant);

                log.info("Inventory restored for variant {}: New OnHand: {}, New Available: {}", 
                        variant.getTitle(), variant.getInventoryAdvance().getQtyOnhand(), 
                        variant.getInventoryAdvance().getQtyAvailable());
            } catch (Exception e) {
                log.error("Failed to restore inventory for variant ID: {}", variantId, e);
            }
        });

        log.info("Inventory restored for returned shipment: {}", shipment.getId());
    }

    private void handleCancelledStatus(Shipment shipment) {
        handleReturnedStatus(shipment);
        log.info("Shipment {} cancelled and inventory restored", shipment.getId());
    }

    /**
     * Synchronizes order fulfillment status based on all shipments for the order
     * Logic:
     * - If all shipments are DELIVERED -> order.fulfillment_status = FULFILLED
     * - If some shipments are DELIVERED -> order.fulfillment_status = PARTIAL
     * - If no shipments are DELIVERED -> order.fulfillment_status = UNFULFILLED
     * Also updates line items fulfillment status based on shipment items
     * Additionally updates order processing status to COMPLETED when all shipments are delivered
     */
    private void synchronizeOrderFulfillmentStatus(Order order) {
        log.info("Synchronizing fulfillment status for order: {}", order.getOrderCode());
        
        // Get all shipments for this order
        List<Shipment> allShipments = shipmentRepository.findByOrderId(order.getId());
        
        if (allShipments.isEmpty()) {
            log.info("No shipments found for order: {}", order.getOrderCode());
            return;
        }
        
        // Count delivered shipments
        long deliveredShipments = allShipments.stream()
                .filter(s -> s.getStatus() == ShipmentStatus.DELIVERED)
                .count();
        
        // Determine order fulfillment status
        FulfillmentStatus newOrderStatus;
        if (deliveredShipments == 0) {
            newOrderStatus = FulfillmentStatus.UNFULFILLED;
        } else if (deliveredShipments == allShipments.size()) {
            newOrderStatus = FulfillmentStatus.FULFILLED;
        } else {
            newOrderStatus = FulfillmentStatus.PARTIAL;
        }
        
        // Update order fulfillment status
        FulfillmentStatus oldOrderStatus = order.getFulfillmentStatus();
        order.setFulfillmentStatus(newOrderStatus);
        
        // Update order processing status to COMPLETED when all shipments are delivered
        if (deliveredShipments == allShipments.size()) {
            OrderProcessingStatus oldProcessingStatus = order.getOrderProcessingStatus();
            order.setOrderProcessingStatus(OrderProcessingStatus.COMPLETED);
            log.info("Updated order {} processing status from {} to COMPLETED (all shipments delivered)", 
                    order.getOrderCode(), oldProcessingStatus);
        }
        
        // Update line items fulfillment status
        updateLineItemsFulfillmentStatus(order, allShipments);
        
        // Save the order
        orderRepository.save(order);
        
        log.info("Updated order {} fulfillment status from {} to {}", 
                order.getOrderCode(), oldOrderStatus, newOrderStatus);
    }
    
    /**
     * Updates line items fulfillment status based on shipment items
     */
    private void updateLineItemsFulfillmentStatus(Order order, List<Shipment> shipments) {
        for (OrderLineItem lineItem : order.getLineItems()) {
            int totalQuantityShipped = 0;
            int totalQuantityDelivered = 0;
            
            // Calculate quantities from all shipments for this line item
            for (Shipment shipment : shipments) {
                for (ShipmentItem shipmentItem : shipment.getItems()) {
                    if (shipmentItem.getOrderLineItemId().equals(lineItem.getId())) {
                        totalQuantityShipped += shipmentItem.getQuantity();
                        
                        if (shipment.getStatus() == ShipmentStatus.DELIVERED) {
                            totalQuantityDelivered += shipmentItem.getQuantity();
                        }
                    }
                }
            }
            
            // Determine line item fulfillment status
            LineItemFulfillmentStatus newLineItemStatus;
            if (totalQuantityDelivered == 0) {
                newLineItemStatus = LineItemFulfillmentStatus.UNFULFILLED;
            } else if (totalQuantityDelivered >= lineItem.getQuantity()) {
                newLineItemStatus = LineItemFulfillmentStatus.FULFILLED;
            } else {
                newLineItemStatus = LineItemFulfillmentStatus.PARTIALLY_FULFILLED;
            }
            
            LineItemFulfillmentStatus oldLineItemStatus = lineItem.getFulfillmentStatus();
            lineItem.setFulfillmentStatus(newLineItemStatus);
            
            log.debug("Updated line item {} fulfillment status from {} to {} (delivered: {}/{})", 
                    lineItem.getId(), oldLineItemStatus, newLineItemStatus, 
                    totalQuantityDelivered, lineItem.getQuantity());
        }
    }
    
    private ShipmentView mapToView(Shipment shipment) {
        if (shipment == null) {
            return null;
        }
        
        ShipmentView view = new ShipmentView();
        view.setId(shipment.getId());
        view.setCarrierCode(shipment.getCarrierCode());
        view.setCarrierName(shipment.getCarrierName());
        view.setTrackingNumber(shipment.getTrackingNumber());
        view.setTrackingUrl(shipment.getTrackingUrl());
        view.setStatus(shipment.getStatus());
        view.setStatusName(shipment.getStatus() != null ? shipment.getStatus().name() : null);
        view.setCodAmount(shipment.getCodAmount());
        view.setCodStatus(shipment.getCodStatus());
        view.setCodStatusName(shipment.getCodStatus() != null ? shipment.getCodStatus().name() : null);
        view.setPrintCount(shipment.getPrintCount());
        view.setLastPrintedDate(shipment.getLastPrintedDate());
        view.setNotes(shipment.getNotes());
        view.setCreatedAt(shipment.getCreatedAt());
        view.setUpdatedAt(shipment.getUpdatedAt());
        
        if (shipment.getOrder() != null) {
            Order order = shipment.getOrder();
            view.setOrderId(order.getId());
            view.setOrderNumber(order.getOrderCode());
            view.setOrderTotal(order.getTotalPrice());
            view.setOrderFulfillmentStatus(order.getFulfillmentStatus() != null ? 
                    order.getFulfillmentStatus().toJson() : "unfulfilled");
            
            view.setCustomerEmail("");
            view.setCustomerName("");
            view.setShippingAddress("");
            view.setShippingCity("");
            view.setShippingZipCode("");
            
            if (order.getShippingAddress() != null) {
                view.setShippingAddress(order.getShippingAddress().getAddress1());
                view.setShippingCity(order.getShippingAddress().getCity());
            }
        }
        
        if (shipment.getItems() != null && !shipment.getItems().isEmpty()) {
            List<ShipmentItemView> itemViews = new ArrayList<>();
            for (ShipmentItem item : shipment.getItems()) {
                itemViews.add(mapToItemView(item));
            }
            view.setItems(itemViews);
        }
        
        return view;
    }
    
    private ShipmentItemView mapToItemView(ShipmentItem item) {
        if (item == null) {
            return null;
        }
        
        ShipmentItemView view = new ShipmentItemView();
        view.setId(item.getId());
        view.setOrderLineItemId(item.getOrderLineItemId());
        view.setProductId(item.getProductId());
        view.setVariantId(item.getVariantId());
        view.setQuantity(item.getQuantity());
        
        return view;
    }
} 