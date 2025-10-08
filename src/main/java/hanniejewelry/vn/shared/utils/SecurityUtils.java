package hanniejewelry.vn.shared.utils;

import hanniejewelry.vn.customer.entity.Customer;
import hanniejewelry.vn.order.entity.Order;
import hanniejewelry.vn.shared.exception.BaseMessageType;
import hanniejewelry.vn.shared.exception.BizException;
import hanniejewelry.vn.user.entity.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.UUID;

@Slf4j
public class SecurityUtils {
    
    /**
     * Check if the current user is authenticated
     * @return true if authenticated, false otherwise
     */
    public static boolean isAuthenticated() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication != null && authentication.isAuthenticated() && 
               authentication.getPrincipal() instanceof User;
    }

    /**
     * Get the current authenticated user
     * @return the current User
     * @throws BizException if no authenticated user is found
     */
    public static User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new BizException(BaseMessageType.UNAUTHORIZED, "No authenticated user found");
        }
        
        Object principal = authentication.getPrincipal();
        if (!(principal instanceof User)) {
            throw new BizException(BaseMessageType.UNAUTHORIZED, "Invalid user authentication");
        }
        
        return (User) principal;
    }
    
    /**
     * Get the current customer associated with the authenticated user
     * @return the Customer entity
     * @throws BizException if no customer profile is found
     */
    public static Customer getCurrentCustomer() {
        User user = getCurrentUser();
        Customer customer = user.getCustomer();
        
        if (customer == null) {
            throw new BizException(BaseMessageType.NOT_FOUND, "No customer profile found for the authenticated user");
        }
        
        log.debug("Retrieved current customer: ID={}, email={}", customer.getId(), customer.getEmail());
        return customer;
    }
    
    /**
     * Get the current customer ID if authenticated, or null if not
     * @return the UUID of the current customer, or null if not authenticated
     */
    public static UUID getCurrentCustomerId() {
        try {
            if (isAuthenticated()) {
                Customer customer = getCurrentCustomer();
                UUID customerId = customer.getId();
                log.debug("Current customer ID: {}", customerId);
                return customerId;
            }
        } catch (Exception e) {
            log.debug("No authenticated customer found: {}", e.getMessage());
        }
        return null;
    }
    
    /**
     * Associate an order with the current customer if authenticated
     * @param order The order to associate with the customer
     * @return true if association was successful, false otherwise
     */
    public static boolean associateOrderWithCurrentCustomer(Order order) {
        if (order == null) {
            log.warn("Cannot associate null order with customer");
            return false;
        }
        
        UUID customerId = getCurrentCustomerId();
        if (customerId != null) {
            order.setUserId(customerId);
            log.info("Associated order {} with authenticated customer ID: {}", 
                    order.getOrderNumber(), customerId);
            return true;
        }
        
        log.debug("No authenticated customer to associate with order {}", order.getOrderNumber());
        return false;
    }
} 