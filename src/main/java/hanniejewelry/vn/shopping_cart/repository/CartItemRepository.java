package hanniejewelry.vn.shopping_cart.repository;

import hanniejewelry.vn.shopping_cart.entity.CartItem;
import hanniejewelry.vn.shared.infrastructure.impl.BaseRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface CartItemRepository extends BaseRepository<CartItem, UUID> {
    
    List<CartItem> findByCartId(UUID cartId);
    
    Optional<CartItem> findByCartIdAndProductIdAndVariantIdValue(UUID cartId, UUID productId, Long variantId);
    
    void deleteByCartId(UUID cartId);
    
    void deleteByCartIdAndId(UUID cartId, UUID itemId);
} 