package hanniejewelry.vn.order.api.mapper;

import hanniejewelry.vn.order.api.dto.LineItemDTO;
import hanniejewelry.vn.shopping_cart.view.ShoppingCartView;

import java.util.List;
import java.util.stream.Collectors;


public class LineItemMapper {


    public static LineItemDTO fromCartItemView(ShoppingCartView.CartItemView item) {
        return LineItemDTO.builder()
                .id(item.getId())
                .variantId(item.getVariantId())
                .productId(item.getProductId())
                .productTitle(item.getProductTitle())
                .variantTitle(item.getVariantTitle())
                .imageUrl(item.getImage())
                .price(item.getPrice())
                .priceOriginal(item.getPrice())
                .linePrice(item.getLinePrice())
                .quantity(item.getQuantity())
                .weight(0.0)
                .lineWeight(0.0)
                .lineAmount(item.getLinePrice())
                .requiresShipping(true)
                .notAllowPromotion(false)
                .discountAllocations(List.of())
                .attributes(List.of())
                .build();
    }


    public static List<LineItemDTO> fromCartItemViewList(List<ShoppingCartView.CartItemView> items) {
        if (items == null) {
            return List.of();
        }
        
        return items.stream()
                .map(LineItemMapper::fromCartItemView)
                .collect(Collectors.toList());
    }
} 