package hanniejewelry.vn.product.view;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.blazebit.persistence.view.EntityView;
import com.blazebit.persistence.view.IdMapping;
import com.blazebit.persistence.view.Mapping;
import hanniejewelry.vn.product.entity.ProductVariant;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@EntityView(ProductVariant.class)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public interface ProductVariantView {
    @IdMapping
    Long getId();
    String getTitle();
    BigDecimal getPrice();
    BigDecimal getCompareAtPrice();
    String getSku();
    String getBarcode();
    BigDecimal getGrams();
    Integer getPosition();
    Integer getInventoryQuantity();
    String getInventoryPolicy();
    String getInventoryManagement();
    Boolean getRequiresShipping();
    Boolean getTaxable();
    String getOption1();
    String getOption2();
    String getOption3();
    UUID getImageId();
    Instant getCreatedAt();
    Instant getUpdatedAt();
    InventoryAdvanceView getInventoryAdvance();

    @Mapping("product.id")
    UUID getProductId();
}
