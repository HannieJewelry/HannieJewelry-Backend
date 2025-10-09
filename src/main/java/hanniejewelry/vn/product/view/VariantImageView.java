package hanniejewelry.vn.product.view;

import com.blazebit.persistence.view.EntityView;
import com.blazebit.persistence.view.IdMapping;
import com.blazebit.persistence.view.Mapping;
import com.fasterxml.jackson.annotation.JsonProperty;
import hanniejewelry.vn.product.entity.ProductVariant;

import java.util.UUID;

@EntityView(ProductVariant.class)
public interface VariantImageView {

    @IdMapping
    @JsonProperty("variant_id") // đổi tên trường khi serialize JSON
    UUID getId();

    @JsonProperty("image_id")
    UUID getImageId();

    @Mapping("image.src")
    @JsonProperty("image_src")
    String getImageSrc();
}
