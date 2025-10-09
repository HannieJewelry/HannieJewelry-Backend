package hanniejewelry.vn.product.view;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.blazebit.persistence.view.EntityView;
import com.blazebit.persistence.view.IdMapping;
import hanniejewelry.vn.product.entity.Product;

import java.time.Instant;
import java.util.List;
import java.util.UUID;


@EntityView(Product.class)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public interface ProductView {
    @IdMapping
    UUID getId();

    String getTitle();
    String getBodyHtml();
    String getBodyPlain();
    String getVendor();
    String getProductType();
    String getTags();
    String getTemplateSuffix();
    Instant getPublishedAt();
    String getPublishedScope();
    Boolean getOnlyHideFromList();
    Boolean getNotAllowPromotion();
    Instant getCreatedAt();
    Instant getUpdatedAt();
    String getHandle();
    List<ProductVariantView> getVariants();
    List<ProductOptionView> getOptions();
    List<ProductImageView> getImages();
    List<CollectView> getCollects();
}
