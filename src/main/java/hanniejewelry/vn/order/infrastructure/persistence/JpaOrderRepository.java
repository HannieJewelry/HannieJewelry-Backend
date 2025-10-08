package hanniejewelry.vn.order.infrastructure.persistence;

import hanniejewelry.vn.order.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface JpaOrderRepository extends JpaRepository<Order, UUID> {
    @Query("SELECT MAX(CAST(SUBSTRING(o.orderCode, 3) AS integer)) FROM Order o")
    Optional<Integer> findMaxOrderNumber();
    
    Optional<Order> findByOrderCode(String orderCode);
    
    Optional<Order> findByOrderNumber(String orderNumber);
    
    // Find orders by cart token
    List<Order> findByCartTokenOrderByCreatedAtDesc(String cartToken);
    
    // Fetch order with shipping and billing addresses
    @Query("SELECT o FROM Order o " +
           "LEFT JOIN FETCH o.shippingAddress " +
           "LEFT JOIN FETCH o.billingAddress " +
           "WHERE o.id = :id")
    Optional<Order> findByIdWithAddresses(UUID id);
    
    // Fetch order with line items
    @Query("SELECT o FROM Order o " +
           "LEFT JOIN FETCH o.lineItems " +
           "WHERE o.id = :id")
    Optional<Order> findByIdWithLineItems(UUID id);
    
    // Fetch order with shipping lines
    @Query("SELECT o FROM Order o " +
           "LEFT JOIN FETCH o.shippingLines " +
           "WHERE o.id = :id")
    Optional<Order> findByIdWithShippingLines(UUID id);
    
    // Fetch order with discount codes
    @Query("SELECT o FROM Order o " +
           "LEFT JOIN FETCH o.discountCodes " +
           "WHERE o.id = :id")
    Optional<Order> findByIdWithDiscountCodes(UUID id);
    
    // Fetch order with transactions
    @Query("SELECT o FROM Order o " +
           "LEFT JOIN FETCH o.transactions " +
           "WHERE o.id = :id")
    Optional<Order> findByIdWithTransactions(UUID id);
    
    // Fetch order with note attributes
    @Query("SELECT o FROM Order o " +
           "LEFT JOIN FETCH o.noteAttributes " +
           "WHERE o.id = :id")
    Optional<Order> findByIdWithNoteAttributes(UUID id);

    List<Order> findByUserIdOrderByCreatedAtDesc(UUID id);

    List<Order> findByCustomerId(UUID customerId);
    
    List<Order> findByCustomerIdOrderByCreatedAtDesc(UUID customerId);

}