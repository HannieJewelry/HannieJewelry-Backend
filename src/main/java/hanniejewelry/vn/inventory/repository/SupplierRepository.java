package hanniejewelry.vn.inventory.repository;

import hanniejewelry.vn.inventory.entity.Supplier;
import hanniejewelry.vn.shared.infrastructure.impl.BaseRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface SupplierRepository extends BaseRepository<Supplier, UUID> {
    
    Optional<Supplier> findBySupplierName(String supplierName);
    
    Optional<Supplier> findByEmail(String email);
} 