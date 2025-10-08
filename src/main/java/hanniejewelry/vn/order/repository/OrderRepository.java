package hanniejewelry.vn.order.repository;

import hanniejewelry.vn.order.entity.Order;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface OrderRepository {
    Optional<Order> findById(UUID id);
    Optional<Order> findByOrderCode(String orderCode);
    Optional<Order> findByOrderNumber(String orderNumber);
    Optional<Integer> findMaxOrderNumber();
    Order save(Order order);
    void deleteById(UUID id);
    List<Order> findByCartToken(String cartToken);

    List<Order> findByUserIdOrderByCreatedAtDesc(UUID id);

    List<Order> findByCustomerId(UUID customerId);


    List<Order> findByCustomerIdOrderByCreatedAtDesc(UUID customerId);
}