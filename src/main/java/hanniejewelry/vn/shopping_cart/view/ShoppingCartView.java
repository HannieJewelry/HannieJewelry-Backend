package hanniejewelry.vn.shopping_cart.view;

import com.blazebit.persistence.view.EntityView;
import com.blazebit.persistence.view.IdMapping;
import com.blazebit.persistence.view.Mapping;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import hanniejewelry.vn.shopping_cart.entity.CartItem;
import hanniejewelry.vn.shopping_cart.entity.ShoppingCart;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@EntityView(ShoppingCart.class)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public interface ShoppingCartView {

    @IdMapping
    UUID getId();
    
    String getToken();
    
    Integer getItemCount();
    
    BigDecimal getTotalPrice();
    
    @Mapping("items")
    List<CartItemView> getItems();
    
    Map<String, String> getAttributes();
    
    String getNote();
    
    @EntityView(CartItem.class)
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    interface CartItemView {
        @IdMapping
        UUID getId();
        
        Integer getQuantity();
        
        BigDecimal getPrice();
        
        BigDecimal getLinePrice();
        
        @Mapping("variant.id")
        Long getVariantId();
        
        @Mapping("variant.product.id")
        UUID getProductId();
        
        @Mapping("variant.product.title")
        String getProductTitle();
        
        @Mapping("variant.title")
        String getVariantTitle();
        
        @Mapping("variant.sku")
        String getSku();
        
        String getImage();
        
    }
} 