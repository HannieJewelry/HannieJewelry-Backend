package hanniejewelry.vn.inventory.repository;

import hanniejewelry.vn.inventory.entity.Location;
import hanniejewelry.vn.shared.infrastructure.impl.BaseRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface LocationRepository extends BaseRepository<Location, UUID> {
    
    Optional<Location> findByName(String name);
    
    List<Location> findByIsShippingLocationTrue();
    
    Optional<Location> findByIsPrimaryLocationTrue();
} 