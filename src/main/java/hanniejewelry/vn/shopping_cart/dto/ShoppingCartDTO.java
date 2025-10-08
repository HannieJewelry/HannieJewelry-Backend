package hanniejewelry.vn.shopping_cart.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import hanniejewelry.vn.shopping_cart.view.ShoppingCartView;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.IntStream;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@Slf4j
public class ShoppingCartDTO {
    private UUID id;
    private String token;
    private Integer itemCount;
    private BigDecimal totalPrice;
    private List<CartItemDTO> items = new ArrayList<>();
    private Map<String, String> attributes;

    public static ShoppingCartDTO fromShoppingCartView(ShoppingCartView view) {
        ShoppingCartDTO dto = ShoppingCartDTO.builder()
                .id(view.getId())
                .token(view.getToken())
                .itemCount(view.getItemCount())
                .totalPrice(view.getTotalPrice())
                .attributes(view.getAttributes())
                .build();
                
        if (view.getItems() != null) {
            List<CartItemDTO> itemsWithLine = IntStream.range(0, view.getItems().size())
                .mapToObj(i -> {
                    CartItemDTO item = CartItemDTO.fromCartItemView(view.getItems().get(i), i + 1);
                    log.debug("Created CartItemDTO with line={}", item.getLine());
                    return item;
                })
                .collect(Collectors.toList());
            
            dto.setItems(itemsWithLine);
            log.debug("Created {} items with line numbers", itemsWithLine.size());
        }
        
        return dto;
    }
}