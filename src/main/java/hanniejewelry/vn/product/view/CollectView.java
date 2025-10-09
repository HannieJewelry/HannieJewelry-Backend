package hanniejewelry.vn.product.view;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.blazebit.persistence.view.EntityView;
import com.blazebit.persistence.view.IdMapping;
import hanniejewelry.vn.product.entity.Collect;

import java.util.UUID;

@EntityView(Collect.class)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public interface CollectView {
    @IdMapping
    UUID getId();
    
    Integer getPosition();
    String getSortValue();
    
    CustomCollectionSlimView getCollection();
} 