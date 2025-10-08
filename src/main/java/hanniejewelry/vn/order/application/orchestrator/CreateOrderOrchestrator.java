package hanniejewelry.vn.order.application.orchestrator;

import hanniejewelry.vn.customer.api.CustomerFacade;
import hanniejewelry.vn.customer.entity.Customer;
import hanniejewelry.vn.order.dto.OrderRequest;
import hanniejewelry.vn.order.application.service.*;
import hanniejewelry.vn.order.domain.event.OrderCreatedEvent;
import hanniejewelry.vn.order.entity.Order;
import hanniejewelry.vn.product.api.ProductFacade;
import hanniejewelry.vn.product.entity.Product;
import hanniejewelry.vn.product.entity.ProductVariant;
import hanniejewelry.vn.promotion.api.PromotionFacade;
import hanniejewelry.vn.shipping.api.ShippingFacade;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class CreateOrderOrchestrator {
    private final CustomerFacade customerFacade;
    private final ProductFacade productFacade;
    private final OrderItemService orderItemService;
    private final ShippingFacade shippingFacade;
    private final PromotionFacade promotionFacade;
    private final OrderService orderService;

    @Transactional
    public void handleOrderCreation(OrderCreatedEvent evt, String userEmail) {
        log.info("Orchestrating order creation. EventId: {}, UserEmail: {}", evt.orderId(), userEmail);

        OrderRequest.OrderInfo orderInfo = evt.orderRequest().getOrder();

        // Find or create customer based on order information
     CustomerFacade.CustomerData customerData = customerFacade.findCustomerData(orderInfo);
        Customer customer = customerData.getCustomer();
        
        if (customer != null) {
            log.info("Found customer for order: ID={}, email={}, phone={}", 
                customer.getId(), customer.getEmail(), customer.getPhone());
        } else {
            log.warn("No customer found for order. Creating with email={}, phone={}", 
                orderInfo.getEmail(), 
                orderInfo.getShippingAddress() != null ? orderInfo.getShippingAddress().getPhone() : null);
        }

        Map<Long, ProductVariant> variantsMap = productFacade.loadVariants(orderInfo);
        Map<UUID, Product> productsMap = productFacade.loadProducts(variantsMap);

        if (!productFacade.validateVariants(variantsMap, evt.orderId())) {
            log.warn("Order creation aborted due to invalid product variants");
            return;
        }

        BigDecimal lineItemsTotal = productFacade.calculateLineItemsTotal(orderInfo, variantsMap);
        BigDecimal shippingTotal = orderItemService.calculateShippingTotal(orderInfo);
        BigDecimal discountTotal = promotionFacade.calculateDiscountTotal(orderInfo);

        // Create order with customer ID if available
        Order order = orderService.createOrder(evt.orderId(), orderInfo, lineItemsTotal,
                shippingTotal, discountTotal, userEmail);
                
        // Set customer ID in order if customer exists
        if (customer != null) {
            order.setUserId(customer.getId());
            log.info("Associated order {} with customer ID: {}", order.getOrderNumber(), customer.getId());
        }

        orderItemService.createLineItems(order, orderInfo, variantsMap, productsMap);
        orderItemService.createShippingLines(order, orderInfo);
        promotionFacade.createDiscountCodes(order, orderInfo);
        shippingFacade.setShippingAddress(order, orderInfo, customerData.getDefaultAddress());
        shippingFacade.setBillingAddress(order, orderInfo, customerData.getDefaultAddress());
        orderItemService.createNoteAttributes(order, orderInfo);
        orderItemService.createInitialTransaction(order, orderInfo);

        // Save the order with customer ID
        orderService.saveOrder(order);
        
        // Update customer order information if customer exists
        // NOTE: Let CustomerService handle the order count increment
        if (customerData.hasCustomer()) {
            try {
                // Let CustomerService handle all the updates including order count
                customerFacade.updateCustomerOrderInfo(customer, order);
                log.info("Updated customer {} with order information: ordersCount={}, lastOrderDate={}", 
                    customer.getId(), customer.getOrdersCount(), customer.getLastOrderDate());
            } catch (Exception e) {
                log.error("Failed to update customer information: {}", e.getMessage(), e);
            }
        }

        orderItemService.updateInventoryQuantities(order);

        log.info("Order creation orchestration completed successfully: {}", order.getOrderNumber());
    }
} 