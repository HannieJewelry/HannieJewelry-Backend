package hanniejewelry.vn.product.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class ProductClientResponse {
    private String bodyHtml;
    private Instant createdAt;
    private String handle;
    private UUID id;
    private String productType;
    private Instant publishedAt;
    private String publishedScope;
    private String templateSuffix;
    private String title;
    private Instant updatedAt;
    private String vendor;
    private Boolean notAllowPromotion;
    private Boolean available;
    private String tags;
    private Integer soleQuantity;
    private List<ProductImageResponse> images;
    private ProductImageResponse image;
    private List<ProductOptionResponse> options;
    private List<ProductVariantResponse> variants;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public static class ProductImageResponse {
        private Instant createdAt;
        private UUID id;
        private Integer position;
        private UUID productId;
        private Instant updatedAt;
        private String src;
        private String alt;
        private List<Long> variantIds;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public static class ProductOptionResponse {
        private String name;
        private Integer position;
        private UUID productId;
        private List<String> values;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public static class ProductVariantResponse {
        private String barcode;
        private String compareAtPrice;

        private Integer grams;
        private Long id;
        private String inventoryManagement;
        private String inventoryPolicy;
        private String option1;
        private String option2;
        private String option3;
        private Integer position;
        private String price;
        private UUID productId;
        private Boolean requiresShipping;
        private String sku;
        private Boolean taxable;
        private String title;
        private Instant updatedAt;
        private Integer inventoryQuantity;
        private Integer oldInventoryQuantity;
        private UUID imageId;
        private Integer weight;
        private Boolean available;
        private String weightUnit;
    }
} 