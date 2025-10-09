package hanniejewelry.vn.product.repository;

import hanniejewelry.vn.product.entity.Collect;
import hanniejewelry.vn.product.entity.CustomCollection;
import hanniejewelry.vn.product.entity.Product;
import hanniejewelry.vn.shared.infrastructure.impl.BaseRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface CollectRepository extends BaseRepository<Collect, UUID> {
    List<Collect> findByCollectionOrderByPosition(CustomCollection collection);
    
    @Query("SELECT COUNT(c) FROM Collect c WHERE c.collection = :collection")
    int countByCollection(@Param("collection") CustomCollection collection);
    
    Optional<Collect> findByCollectionAndProduct(CustomCollection collection, Product product);
    
}