package hanniejewelry.vn.product.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

import java.math.BigDecimal;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductVariantRequest {
    private String title;
    @DecimalMin("0.0")
    @Builder.Default
    private BigDecimal price = BigDecimal.ZERO;

    @DecimalMin("0.0")
    @Builder.Default
    @JsonProperty("compare_at_price")
    private BigDecimal compareAtPrice = BigDecimal.ZERO;
    
    @DecimalMin("0.0")
    @Builder.Default
    @JsonProperty("cost_price")
    private BigDecimal costPrice = BigDecimal.ZERO;

    private String sku;
    private String barcode;

    @DecimalMin("0.0")
    @Builder.Default
    private BigDecimal grams = BigDecimal.ZERO;

    @Min(0)
    @Builder.Default
    @JsonProperty("inventory_quantity")
    private Integer inventoryQuantity = 0;

    @JsonProperty("inventory_policy")
    private String inventoryPolicy;
    
    @JsonProperty("inventory_management")
    private String inventoryManagement;
    
    @Builder.Default
    @JsonProperty("is_tracking_inventory")
    private boolean isTrackingInventory = false;
    
    @Builder.Default
    @JsonProperty("allow_purchase_when_sold_out")
    private boolean allowPurchaseWhenSoldOut = false;
    
    private List<InventoryRequest> inventories;
    
    @Builder.Default
    @JsonProperty("requires_shipping")
    private boolean requiresShipping = false;
    
    @Builder.Default
    private boolean taxable = false;
    

    private String option1;
    private String option2;
    private String option3;
}
