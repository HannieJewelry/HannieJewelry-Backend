package hanniejewelry.vn.product.view;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.blazebit.persistence.view.EntityView;
import com.blazebit.persistence.view.IdMapping;
import com.blazebit.persistence.view.Mapping;
import hanniejewelry.vn.product.entity.ProductOption;

import java.util.UUID;

@EntityView(ProductOption.class)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public interface ProductOptionView {
    @IdMapping
    UUID getId();
    String getName();
    Integer getPosition();

    @Mapping("product.id")
    UUID getProductId();
}
