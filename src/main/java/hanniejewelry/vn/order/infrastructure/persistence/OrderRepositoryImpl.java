package hanniejewelry.vn.order.infrastructure.persistence;


import lombok.RequiredArgsConstructor;
import hanniejewelry.vn.order.entity.Order;
import hanniejewelry.vn.order.repository.OrderRepository;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class OrderRepositoryImpl implements OrderRepository {

    private final JpaOrderRepository jpaOrderRepository;

    @Override
    @Transactional(readOnly = true)
    public Optional<Order> findById(UUID id) {
        Optional<Order> orderOpt = jpaOrderRepository.findByIdWithAddresses(id);
        
        if (orderOpt.isEmpty()) {
            return Optional.empty();
        }
        
        Order order = orderOpt.get();
        
        jpaOrderRepository.findByIdWithLineItems(id)
            .ifPresent(o -> order.setLineItems(o.getLineItems()));
            
        jpaOrderRepository.findByIdWithShippingLines(id)
            .ifPresent(o -> order.setShippingLines(o.getShippingLines()));
            
        jpaOrderRepository.findByIdWithDiscountCodes(id)
            .ifPresent(o -> order.setDiscountCodes(o.getDiscountCodes()));
            
        jpaOrderRepository.findByIdWithTransactions(id)
            .ifPresent(o -> order.setTransactions(o.getTransactions()));
            
        jpaOrderRepository.findByIdWithNoteAttributes(id)
            .ifPresent(o -> order.setNoteAttributes(o.getNoteAttributes()));
        
        return Optional.of(order);
    }

    @Override
    public Optional<Order> findByOrderCode(String orderCode) {
        return jpaOrderRepository.findByOrderCode(orderCode);
    }

    @Override
    public Optional<Order> findByOrderNumber(String orderNumber) {
        return jpaOrderRepository.findByOrderNumber(orderNumber);
    }

    @Override
    public Optional<Integer> findMaxOrderNumber() {
        return jpaOrderRepository.findMaxOrderNumber();
    }

    @Override
    public Order save(Order order) {
        return jpaOrderRepository.save(order);
    }

    @Override
    public void deleteById(UUID id) {
        jpaOrderRepository.deleteById(id);
    }
    
    @Override
    public List<Order> findByCartToken(String cartToken) {
        return jpaOrderRepository.findByCartTokenOrderByCreatedAtDesc(cartToken);
    }

    @Override
    public List<Order> findByUserIdOrderByCreatedAtDesc(UUID id) {
        return jpaOrderRepository.findByUserIdOrderByCreatedAtDesc(id);
    }

    @Override
    public List<Order> findByCustomerId(UUID customerId) {
        return jpaOrderRepository.findByCustomerId(customerId);
    }

    @Override
    public List<Order> findByCustomerIdOrderByCreatedAtDesc(UUID customerId) {
        return jpaOrderRepository.findByCustomerIdOrderByCreatedAtDesc(customerId);
    }


}
