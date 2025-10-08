package hanniejewelry.vn.shopping_cart.repository;

import hanniejewelry.vn.shopping_cart.entity.ShoppingCart;
import hanniejewelry.vn.shared.infrastructure.impl.BaseRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface ShoppingCartRepository extends BaseRepository<ShoppingCart, UUID> {
    
    Optional<ShoppingCart> findByToken(String token);
    
    Optional<ShoppingCart> findByCustomerId(UUID customerId);
    
    void deleteByToken(String token);
    
    boolean existsByToken(String token);
} 