package hanniejewelry.vn.order.domain.model.repository;

import hanniejewelry.vn.order.domain.model.entity.OrderProcessingError;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface OrderProcessingErrorRepository extends JpaRepository<OrderProcessingError, UUID> {
    
    Optional<OrderProcessingError> findFirstByOrderIdOrderByOccurredAtDesc(UUID orderId);
    

    List<OrderProcessingError> findByOrderIdOrderByOccurredAtDesc(UUID orderId);
} 