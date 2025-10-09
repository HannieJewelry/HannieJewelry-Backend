package hanniejewelry.vn.product.view;

import com.blazebit.persistence.view.EntityView;
import com.blazebit.persistence.view.IdMapping;
import com.blazebit.persistence.view.Mapping;
import hanniejewelry.vn.product.entity.ProductImage;

import java.time.Instant;
import java.util.Set;
import java.util.UUID;

@EntityView(ProductImage.class)
public interface ProductImageView {
    @IdMapping
    UUID getId();
    String getSrc();
    String getFilename();
    Integer getPosition();
    Set<Long> getVariantIds();

    @Mapping("product.id")
    UUID getProductId();

    Instant getCreatedAt();
    Instant getUpdatedAt();


}
