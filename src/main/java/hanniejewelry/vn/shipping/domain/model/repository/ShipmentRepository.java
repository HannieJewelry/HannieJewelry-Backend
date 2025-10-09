package hanniejewelry.vn.shipping.domain.model.repository;

import hanniejewelry.vn.shipping.domain.model.entity.Shipment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ShipmentRepository extends JpaRepository<Shipment, UUID> {
    
    List<Shipment> findByOrderId(UUID orderId);
    
    List<Shipment> findByTrackingNumber(String trackingNumber);
} 