package hanniejewelry.vn.order.repository;

import hanniejewelry.vn.order.ShippingMethod;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ShippingMethodRepository extends JpaRepository<ShippingMethod, Long> {
    
    List<ShippingMethod> findByActiveTrue();
    
    List<ShippingMethod> findByActiveTrueOrderByDisplayOrderAsc();
} 