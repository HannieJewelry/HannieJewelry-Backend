package hanniejewelry.vn.customer.usecase.customer;

import hanniejewelry.vn.customer.entity.Customer;
import hanniejewelry.vn.customer.repository.CustomerRepository;
import hanniejewelry.vn.customer.view.CustomerView;
import hanniejewelry.vn.order.entity.Order;
import hanniejewelry.vn.order.repository.OrderRepository;
import hanniejewelry.vn.shared.exception.BaseMessageType;
import hanniejewelry.vn.shared.exception.BizException;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.blazebit.persistence.view.EntityViewManager;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class GetCustomerByIdUseCase {
    private final CustomerRepository customerRepository;
    private final OrderRepository orderRepository;
    private final EntityViewManager evm;
    private final EntityManager em;

    @Transactional
    public CustomerView execute(UUID id) {
        if (!customerRepository.existsById(id)) {
            throw new BizException(BaseMessageType.NOT_FOUND, "Customer not found with id: {0}", id);
        }

        // Get the customer entity
        Customer customer = customerRepository.findById(id).orElseThrow();
        log.info("Found customer: ID={}, email={}, orders_count={}, last_order_date={}", 
            id, customer.getEmail(), customer.getOrdersCount(), customer.getLastOrderDate());

        // Fix inconsistencies in customer data
        boolean needsUpdate = false;
        
        // Check if customer has orders but null or incorrect last_order_date
        if ((customer.getOrdersCount() == null || customer.getOrdersCount() > 0) && 
            (customer.getLastOrderDate() == null || customer.getLastOrderId() == null)) {
            
            // Find the most recent order for this customer
            List<Order> customerOrders = orderRepository.findByUserIdOrderByCreatedAtDesc(id);
            log.info("Found {} orders for customer {}", customerOrders.size(), id);
            
            if (!customerOrders.isEmpty()) {
                Order latestOrder = customerOrders.get(0); // First is most recent due to ordering
                
                // Update customer with latest order info
                if (customer.getLastOrderDate() == null) {
                    customer.setLastOrderDate(latestOrder.getCreatedAt() != null ? 
                        latestOrder.getCreatedAt() : Instant.now());
                    log.info("Fixed last_order_date for customer {}: {}", id, customer.getLastOrderDate());
                    needsUpdate = true;
                }
                
                if (customer.getLastOrderId() == null) {
                    customer.setLastOrderId(latestOrder.getId());
                    customer.setLastOrderName(latestOrder.getOrderNumber());
                    log.info("Fixed last_order_id for customer {}: {}", id, customer.getLastOrderId());
                    needsUpdate = true;
                }
                
                // Fix orders count if needed
                if (customer.getOrdersCount() == null || customer.getOrdersCount() != customerOrders.size()) {
                    customer.setOrdersCount(customerOrders.size());
                    log.info("Fixed orders_count for customer {}: {}", id, customer.getOrdersCount());
                    needsUpdate = true;
                }
            }
        }
        
        // Save the customer if updates were made
        if (needsUpdate) {
            customer = customerRepository.save(customer);
            log.info("Updated customer profile with fixed data: orders_count={}, last_order_date={}", 
                customer.getOrdersCount(), customer.getLastOrderDate());
        }
        
        // Return the customer view
        CustomerView customerView = evm.find(em, CustomerView.class, id);
        log.info("Returning customer view with orders_count={}, last_order_date={}", 
            customerView.getOrdersCount(), customerView.getLastOrderDate());
        return customerView;
    }
}
