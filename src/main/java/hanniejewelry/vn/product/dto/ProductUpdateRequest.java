package hanniejewelry.vn.product.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@JsonIgnoreProperties(ignoreUnknown = true)
public class ProductUpdateRequest {
    private UUID id;
    
    @NotBlank
    private String title;
    
    private String productType;
    private String vendor;
    private String bodyHtml;
    private String bodyPlain;
    private boolean onlyHideFromList;
    private boolean notAllowPromotion;
    private Instant publishedAt;
    private String publishedScope;
    private String tags;
    private String templateSuffix;
    
    @Valid
    private List<ProductVariantDTO> variants;
    
    @Valid
    private List<ProductOptionDTO> options;
    
    @Data
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class ProductVariantDTO {
        private Long id;
        private String title;
        private BigDecimal price;
        private BigDecimal compareAtPrice;
        private String sku;
        private String barcode;
        private BigDecimal grams;
        private Integer position;
        private boolean requiresShipping;
        private boolean taxable;
        private String option1;
        private String option2;
        private String option3;
        private UUID productId;
    }
    
    @Data
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class ProductOptionDTO {
        private UUID id;
        private String name;
        private Integer position;
        private UUID productId;
    }
} 