package hanniejewelry.vn.product.repository;

import hanniejewelry.vn.product.entity.CustomCollection;
import hanniejewelry.vn.shared.infrastructure.impl.BaseRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface CustomCollectionRepository extends BaseRepository<CustomCollection, UUID> {
    Optional<CustomCollection> findByHandle(String handle);

    boolean existsByHandle(String handle);
    
    long count();
} 