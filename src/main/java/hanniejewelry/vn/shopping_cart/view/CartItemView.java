package hanniejewelry.vn.shopping_cart.view;

import com.blazebit.persistence.view.EntityView;
import com.blazebit.persistence.view.IdMapping;
import com.blazebit.persistence.view.Mapping;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import hanniejewelry.vn.shopping_cart.entity.CartItem;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@EntityView(CartItem.class)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public interface CartItemView {

    @IdMapping
    UUID getId();
    
    String getTitle();
    
    String getVariantTitle();
    
    BigDecimal getPrice();
    
    BigDecimal getLinePrice();
    
    BigDecimal getPriceOriginal();
    
    BigDecimal getLinePriceOriginal();
    
    Integer getQuantity();
    
    String getSku();
    
    String getBarcode();
    
    BigDecimal getGrams();
    
    String getProductType();
    
    String getVendor();
    
    Map<String, String> getProperties();
    
    @Mapping("variant.id")
    Long getVariantId();

    @Mapping("product.id")
    UUID getProductId();
    @JsonIgnore
    @Mapping("variant.option1")
    String getOption1();

    @JsonIgnore
    @Mapping("variant.option2")
    String getOption2();

    @JsonIgnore
    @Mapping("variant.option3")
    String getOption3();

    @JsonProperty("variant_options")
    default List<String> getVariantOptions() {
        List<String> result = new ArrayList<>();
        if (getOption1() != null && !getOption1().isEmpty()) result.add(getOption1());
        if (getOption2() != null && !getOption2().isEmpty()) result.add(getOption2());
        if (getOption3() != null && !getOption3().isEmpty()) result.add(getOption3());
        return result;
    }
    Boolean getGiftCard();
    
    @Mapping("product.handle")
    String getUrl();
    
    String getImage();
    
    String getHandle();
    
    Boolean getRequiresShipping();
    
    Boolean getNotAllowPromotion();
    @Mapping("product.title")
    String getProductTitle();
} 