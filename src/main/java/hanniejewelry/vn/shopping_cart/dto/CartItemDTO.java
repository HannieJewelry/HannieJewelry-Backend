package hanniejewelry.vn.shopping_cart.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import hanniejewelry.vn.shopping_cart.view.ShoppingCartView;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.UUID;

/**
 * DTO class for cart items that includes a line number
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class CartItemDTO {
    private UUID id;
    private Integer quantity;
    private BigDecimal price;
    private BigDecimal linePrice;
    private Long variantId;
    private UUID productId;
    private String productTitle;
    private String variantTitle;
    private String sku;
    private String image;
    
    @JsonProperty("line")
    private Integer line;
    

    public static CartItemDTO fromCartItemView(ShoppingCartView.CartItemView view, int lineNumber) {
        return CartItemDTO.builder()
                .id(view.getId())
                .quantity(view.getQuantity())
                .price(view.getPrice())
                .linePrice(view.getLinePrice())
                .variantId(view.getVariantId())
                .productId(view.getProductId())
                .productTitle(view.getProductTitle())
                .variantTitle(view.getVariantTitle())
                .sku(view.getSku())
                .image(view.getImage())
                .line(lineNumber)
                .build();
    }
}