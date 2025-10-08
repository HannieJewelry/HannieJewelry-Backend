package hanniejewelry.vn.product.view;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.blazebit.persistence.view.EntityView;
import com.blazebit.persistence.view.IdMapping;
import hanniejewelry.vn.product.entity.CustomCollection;

import java.time.Instant;
import java.util.UUID;

@EntityView(CustomCollection.class)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public interface CustomCollectionSlimView {
    @IdMapping
    UUID getId();
    
    String getTitle();
    String getHandle();
    Boolean getPublished();
    Instant getPublishedAt();
} 