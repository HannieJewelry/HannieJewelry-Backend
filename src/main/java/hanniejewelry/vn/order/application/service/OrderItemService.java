package hanniejewelry.vn.order.application.service;

import hanniejewelry.vn.order.entity.Order;
import hanniejewelry.vn.order.dto.OrderRequest;
import hanniejewelry.vn.order.domain.model.entity.*;
import hanniejewelry.vn.order.domain.model.enums.LineItemFulfillmentStatus;
import hanniejewelry.vn.product.entity.InventoryAdvance;
import hanniejewelry.vn.product.entity.Product;
import hanniejewelry.vn.product.entity.ProductVariant;
import hanniejewelry.vn.product.repository.ProductVariantRepository;
import hanniejewelry.vn.shared.utils.BigDecimalUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderItemService {

    private final ProductVariantRepository productVariantRepository;


    public void createLineItems(Order order, OrderRequest.OrderInfo orderInfo, 
                               Map<Long, ProductVariant> variantsMap, 
                               Map<UUID, Product> productsMap) {
        if (orderInfo.getLineItems() == null) {
            return;
        }
        
        List<OrderLineItem> lineItems = new ArrayList<>();
        for (OrderRequest.LineItem item : orderInfo.getLineItems()) {
            ProductVariant variant = variantsMap.get(item.getVariantId());
            if (variant == null) {
                continue;
            }
            
            Product product = variant.getProduct();
            if (product == null && variant.getProduct() != null && variant.getProduct().getId() != null) {
                product = productsMap.get(variant.getProduct().getId());
            }
            
            BigDecimal pricePromotion = BigDecimalUtils.zero();
            BigDecimal priceOriginal = BigDecimalUtils.of(variant.getPrice());
            
            if (variant.getCompareAtPrice() != null && BigDecimalUtils.isPositive(variant.getCompareAtPrice())) {
                priceOriginal = BigDecimalUtils.of(variant.getCompareAtPrice());
                pricePromotion = BigDecimalUtils.subtract(
                                    BigDecimalUtils.of(variant.getCompareAtPrice()),
                                    BigDecimalUtils.of(variant.getPrice()));
            }
            
            String vendor = product != null ? product.getVendor() : null;
            
            String imageSrc = null;
            if (variant.getImage() != null) {
                imageSrc = variant.getImage().getSrc();
            } else if (product != null && product.getImages() != null && !product.getImages().isEmpty()) {
                imageSrc = product.getImages().get(0).getSrc();
            }
            
            OrderLineItem lineItem = OrderLineItem.builder()
                    .order(order)
                    .quantity(item.getQuantity())
                    .variantId(variant.getId())
                    .productId(product != null ? product.getId() : null)
                    .price(BigDecimalUtils.of(variant.getPrice()))
                    .priceOriginal(priceOriginal)
                    .pricePromotion(pricePromotion)
                    .totalDiscount(BigDecimalUtils.zero())
                    .fulfillmentStatus(LineItemFulfillmentStatus.NOT_FULFILLED)
                    .fulfillableQuantity(item.getQuantity())
                    .requiresShipping(variant.isRequiresShipping())
                    .sku(variant.getSku())
                    .title(variant.getTitle())
                    .variantTitle(variant.getOption1())
                    .vendor(vendor)
                    .type(product != null ? product.getProductType() : null)
                    .name(variant.getTitle())
                    .grams(variant.getGrams() != null ? variant.getGrams().doubleValue() : 0.0)
                    .productExists(true)
                    .notAllowPromotion(product != null && product.isNotAllowPromotion())
                    .barcode(variant.getBarcode())
                    .giftCard(false)
                    .taxable(variant.isTaxable())
                    .maCostAmount(BigDecimalUtils.zero())
                    .actualPrice(BigDecimalUtils.of(variant.getPrice()))
                    .imageSrc(imageSrc)
                    .build();
            
            lineItems.add(lineItem);
            
            log.debug("Added line item: productId={}, variantId={}, sku={}, title={}, price={}",
                    lineItem.getProductId(), lineItem.getVariantId(), lineItem.getSku(), 
                    lineItem.getTitle(), lineItem.getPrice());
        }
        
        order.getLineItems().addAll(lineItems);
    }
    

    public void createShippingLines(Order order, OrderRequest.OrderInfo orderInfo) {
        if (orderInfo.getShippingLines() == null) {
            return;
        }
        
        List<OrderShippingLine> shippingLines = new ArrayList<>();
        for (OrderRequest.ShippingLine shipping : orderInfo.getShippingLines()) {
            OrderShippingLine shippingLine = OrderShippingLine.builder()
                    .order(order)
                    .code(shipping.getCode())
                    .price(BigDecimalUtils.of(shipping.getPrice()))
                    .source(shipping.getSource())
                    .title(shipping.getTitle())
                    .build();
            shippingLines.add(shippingLine);
        }
        
        order.getShippingLines().addAll(shippingLines);
    }
    

    public void createNoteAttributes(Order order, OrderRequest.OrderInfo orderInfo) {
        if (orderInfo.getNoteAttributes() == null) {
            return;
        }
        
        List<OrderNoteAttribute> noteAttributes = new ArrayList<>();
        for (OrderRequest.NoteAttribute note : orderInfo.getNoteAttributes()) {
            OrderNoteAttribute noteAttribute = OrderNoteAttribute.builder()
                    .order(order)
                    .name(note.getName())
                    .value(note.getValue())
                    .build();
            noteAttributes.add(noteAttribute);
        }
        
        order.getNoteAttributes().addAll(noteAttributes);
    }
    

    public void createInitialTransaction(Order order, OrderRequest.OrderInfo orderInfo) {
        OrderTransaction transaction = OrderTransaction.builder()
                .order(order)
                .amount(BigDecimalUtils.of(order.getTotalPrice()))
                .gateway(orderInfo.getGateway())
                .kind("pending")
                .userId(orderInfo.getUserId())
                .locationId(orderInfo.getLocationId())
                .currency("USD")
                .sendEmail(false)
                .build();

        order.getTransactions().add(transaction);
    }
    

    public BigDecimal calculateShippingTotal(OrderRequest.OrderInfo orderInfo) {
        BigDecimal shippingTotal = BigDecimalUtils.zero();
        
        if (orderInfo.getShippingLines() == null) {
            return shippingTotal;
        }
        
        for (OrderRequest.ShippingLine shipping : orderInfo.getShippingLines()) {
            if (shipping.getPrice() != null) {
                shippingTotal = BigDecimalUtils.add(shippingTotal, BigDecimalUtils.of(shipping.getPrice()));
            }
        }
        
        return shippingTotal;
    }


    @Transactional
    public void updateInventoryQuantities(Order order) {
        if (order == null || order.getLineItems() == null || order.getLineItems().isEmpty()) {
            log.info("No inventory update needed - order is null or has no line items");
            return;
        }
        
        log.info("Starting inventory update for order: {} (Order Number: {})", order.getId(), order.getOrderNumber());
        
        for (OrderLineItem lineItem : order.getLineItems()) {
            Long variantId = lineItem.getVariantId();
            if (variantId == null) {
                log.warn("Skipping inventory update for line item - variant ID is null");
                continue;
            }
            
            log.debug("Processing inventory update for variant ID: {}, product: {}, quantity: {}", 
                    variantId, lineItem.getTitle(), lineItem.getQuantity());
            
            ProductVariant variant = productVariantRepository.findById(variantId).orElse(null);
            if (variant == null) {
                log.warn("Cannot update inventory for variant ID {} - not found", variantId);
                continue;
            }
            
            InventoryAdvance inventoryAdvance = variant.getInventoryAdvance();
            if (inventoryAdvance == null) {
                log.info("Creating new InventoryAdvance for variant ID: {}", variantId);
                inventoryAdvance = new InventoryAdvance();
                variant.setInventoryAdvance(inventoryAdvance);
            }
            
            int quantity = lineItem.getQuantity() != null ? lineItem.getQuantity() : 0;
            
            int currentQtyOnhand = inventoryAdvance.getQtyOnhand() != null ? inventoryAdvance.getQtyOnhand() : 0;
            int currentQtyCommited = inventoryAdvance.getQtyCommited() != null ? inventoryAdvance.getQtyCommited() : 0;
            int currentQtyAvailable = inventoryAdvance.getQtyAvailable() != null ? inventoryAdvance.getQtyAvailable() : 0;
            int currentQtyIncoming = inventoryAdvance.getQtyIncoming() != null ? inventoryAdvance.getQtyIncoming() : 0;
            
            log.info("Inventory BEFORE update - Variant: {} ({}), SKU: {}", 
                    variant.getTitle(), variantId, variant.getSku());
            log.info("  → On Hand: {}, Committed: {}, Available: {}, Incoming: {}", 
                    currentQtyOnhand, currentQtyCommited, currentQtyAvailable, currentQtyIncoming);
            
            inventoryAdvance.setQtyCommited(currentQtyCommited + quantity);
            inventoryAdvance.setQtyAvailable(Math.max(0, currentQtyAvailable - quantity));
            
            variant.setInventoryQuantity(Math.max(0, currentQtyOnhand - quantity));
            
            log.info("Inventory CHANGES - Variant: {} ({})", variant.getTitle(), variantId);
            log.info("  → Committed: {} → {} (+{})", 
                    currentQtyCommited, inventoryAdvance.getQtyCommited(), quantity);
            log.info("  → Available: {} → {} (-{})", 
                    currentQtyAvailable, inventoryAdvance.getQtyAvailable(), quantity);
            log.info("  → On Hand: {} → {}", 
                    currentQtyOnhand, variant.getInventoryQuantity());
            
            productVariantRepository.save(variant);
            
            log.info("Inventory updated successfully for variant ID: {} in order: {}", variantId, order.getId());
        }
        
        log.info("Completed inventory update for all items in order: {} (Order Number: {})", 
                order.getId(), order.getOrderNumber());
    }
} 