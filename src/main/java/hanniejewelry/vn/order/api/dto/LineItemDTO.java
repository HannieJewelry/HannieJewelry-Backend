package hanniejewelry.vn.order.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LineItemDTO {
    private UUID id;
    private Integer seq;
    
    @JsonProperty("variant_id")
    private Long variantId;
    
    @JsonProperty("variant_type")
    private String variantType;
    
    @JsonProperty("product_id")
    private UUID productId;
    
    @JsonProperty("product_title")
    private String productTitle;
    
    @JsonProperty("variant_title")
    private String variantTitle;
    
    @JsonProperty("image_url")
    private String imageUrl;
    
    @JsonProperty("price_original")
    private BigDecimal priceOriginal;
    
    private BigDecimal price;
    
    @JsonProperty("line_price")
    private BigDecimal linePrice;
    
    @JsonProperty("line_discount")
    private BigDecimal lineDiscount;
    
    private Integer quantity;
    private Double weight;
    
    @JsonProperty("line_weight")
    private Double lineWeight;
    
    @JsonProperty("line_tax")
    private BigDecimal lineTax;
    
    @JsonProperty("line_amount")
    private BigDecimal lineAmount;
    
    @JsonProperty("available_quantity")
    private Integer availableQuantity;
    
    @JsonProperty("lot_support")
    private Boolean lotSupport;
    
    @JsonProperty("variant_unit_id")
    private Long variantUnitId;
    
    @JsonProperty("unit_ratio")
    private Double unitRatio;
    
    private String unit;
    
    @JsonProperty("requires_shipping")
    private Boolean requiresShipping;
    
    @JsonProperty("not_allow_promotion")
    private Boolean notAllowPromotion;
    
    @JsonProperty("discount_allocations")
    private List<Object> discountAllocations;
    
    private List<Object> attributes;
    
    private Object components;
} 