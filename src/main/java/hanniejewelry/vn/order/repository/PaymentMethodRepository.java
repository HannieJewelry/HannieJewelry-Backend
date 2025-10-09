package hanniejewelry.vn.order.repository;

import hanniejewelry.vn.payment.entity.PaymentMethod;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PaymentMethodRepository extends JpaRepository<PaymentMethod, Long> {
    
    List<PaymentMethod> findByActiveTrue();
    
    List<PaymentMethod> findByActiveTrueOrderByDisplayOrderAsc();
    
    List<PaymentMethod> findByActiveTrueAndOnlineTrue();
    
    List<PaymentMethod> findByActiveTrueAndOnlineFalse();
} 