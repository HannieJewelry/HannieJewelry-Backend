package hanniejewelry.vn.order.application.service;

import hanniejewelry.vn.order.dto.OrderRequest;
import hanniejewelry.vn.order.entity.Order;
import hanniejewelry.vn.order.domain.model.enums.CancelledStatus;
import hanniejewelry.vn.order.domain.model.enums.ClosedStatus;
import hanniejewelry.vn.order.domain.model.enums.ConfirmedStatus;
import hanniejewelry.vn.order.domain.model.enums.FinancialStatus;
import hanniejewelry.vn.order.domain.model.enums.FulfillmentStatus;
import hanniejewelry.vn.order.domain.model.enums.OrderProcessingStatus;
import hanniejewelry.vn.order.repository.OrderRepository;
import hanniejewelry.vn.shared.utils.BigDecimalUtils;
import hanniejewelry.vn.shared.utils.SecurityUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderService {
    private final OrderRepository orderRepository;
    
    public String generateToken() {
        StringBuilder token = new StringBuilder();
        for (int i = 0; i < 32; i++) {
            int randomNum = ThreadLocalRandom.current().nextInt(0, 16);
            token.append(Integer.toHexString(randomNum));
        }
        return token.toString();
    }
    
    /**
     * Format phone number to ensure consistent format with +84 prefix
     */
    private String formatPhoneNumber(String phone) {
        if (phone == null || phone.isEmpty()) {
            return phone;
        }
        
        if (!phone.startsWith("+")) {
            if (phone.startsWith("0")) {
                return "+84" + phone.substring(1);
            } else {
                return "+84" + phone;
            }
        }
        
        return phone;
    }
    
    /**
     * Format email for phone numbers without actual emails
     */
    private String formatEmail(String email, String phone) {
        if (email == null || email.isEmpty()) {
            if (phone != null && !phone.isEmpty()) {
                return phone + "@no-email.local";
            }
            return null;
        }
        
        if (!email.contains("@")) {
            return email + "@no-email.local";
        }
        
        return email;
    }

    @Transactional
    public Order createOrder(UUID orderId, OrderRequest.OrderInfo orderInfo, 
                           BigDecimal lineItemsTotal, BigDecimal shippingTotal, 
                           BigDecimal discountTotal, String userEmail) {
        Integer maxOrderNum = orderRepository.findMaxOrderNumber().orElse(0);
        String orderCode = "DH" + (maxOrderNum + 1);
        String orderNumber = "#" + (10000 + maxOrderNum + 1);
        
        String token = orderInfo.getCartToken();
        if (token == null || token.isEmpty()) {
            token = generateToken();
            log.warn("Cart token not provided in order request, generated new token: {}", token);
        } else {
            log.info("Using cart token from request: {}", token);
        }
        
        BigDecimal totalPrice = BigDecimalUtils.subtract(
                                    BigDecimalUtils.add(lineItemsTotal, shippingTotal),
                                    discountTotal);
        
        // Get name from shipping address or use default
        String orderName = "Customer";
        if (orderInfo.getShippingAddress() != null) {
            String firstName = orderInfo.getShippingAddress().getFirstName();
            String lastName = orderInfo.getShippingAddress().getLastName();
            
            if (firstName != null && !firstName.isEmpty()) {
                orderName = firstName;
                if (lastName != null && !lastName.isEmpty()) {
                    orderName += " " + lastName;
                }
            }
        }
        
        // Format phone number from shipping address if available
        String phone = orderInfo.getShippingAddress() != null ? 
                       formatPhoneNumber(orderInfo.getShippingAddress().getPhone()) : null;
        
        // Format email, using phone as fallback if email is not provided
        String email = formatEmail(orderInfo.getEmail(), phone);
        
        // Use the userId from orderInfo if available, or try to get from current authenticated user
        UUID userId = orderInfo.getUserId();
        if (userId == null) {
            userId = SecurityUtils.getCurrentCustomerId();
            if (userId != null) {
                log.info("Using authenticated customer ID for order: {}", userId);
            }
        }
        
        Order order = Order.builder()
                .id(orderId)
                .orderCode(orderCode)
                .orderNumber(orderNumber)
                .name(orderName)
                .email(email)
                .totalPrice(totalPrice)
                .subtotalPrice(lineItemsTotal)
                .totalDiscounts(discountTotal)
                .totalLineItemsPrice(lineItemsTotal)
                .totalTax(BigDecimalUtils.zero())
                .financialStatus(FinancialStatus.PENDING)
                .fulfillmentStatus(FulfillmentStatus.UNFULFILLED)
                .orderProcessingStatus(OrderProcessingStatus.PENDING)
                .tags(orderInfo.getTags())
                .createdBy(userEmail)
                .gateway(orderInfo.getGateway())
                .note(orderInfo.getNote())
                .source(orderInfo.getSource())
                .sourceName(orderInfo.getSourceName())
                .referringSite(orderInfo.getReferringSite())
                .landingSite(orderInfo.getLandingSite())
                .landingSiteRef(orderInfo.getLandingSiteRef())
                .closedStatus(ClosedStatus.UNCLOSED)
                .cancelledStatus(CancelledStatus.UNCANCELLED)
                .confirmedStatus(ConfirmedStatus.UNCONFIRMED)
                .userId(userId)
                .locationId(orderInfo.getLocationId())
                .contactEmail(email)
                .utmSource(orderInfo.getUtmSource())
                .utmMedium(orderInfo.getUtmMedium())
                .utmCampaign(orderInfo.getUtmCampaign())
                .utmTerm(orderInfo.getUtmTerm())
                .utmContent(orderInfo.getUtmContent())
                .cartToken(token)
                .checkoutToken(token)
                .token(token)
                .buyerAcceptsMarketing(true)
                .currency("USD")
                .taxesIncluded(false)
                .createdAt(Instant.now())
                .build();
        
        if (userId != null) {
            log.info("Order {} created with customer ID: {}", orderNumber, userId);
        } else {
            log.info("Order {} created without customer ID", orderNumber);
        }
        
        return orderRepository.save(order);
    }
    
    @Transactional
    public void saveOrder(Order order) {
        // Ensure customer ID is set if user is authenticated
        if (order.getUserId() == null) {
            UUID customerId = SecurityUtils.getCurrentCustomerId();
            if (customerId != null) {
                order.setUserId(customerId);
                log.info("Updated order {} with authenticated customer ID: {}", 
                        order.getOrderNumber(), customerId);
            }
        }
        
        orderRepository.save(order);
        log.info("Order saved successfully: {}", order.getOrderNumber());
    }
} 