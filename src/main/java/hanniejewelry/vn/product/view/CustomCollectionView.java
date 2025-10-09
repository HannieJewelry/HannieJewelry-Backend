package hanniejewelry.vn.product.view;

import com.blazebit.persistence.view.*;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import hanniejewelry.vn.product.entity.CollectionImage;
import hanniejewelry.vn.product.entity.CustomCollection;

import java.time.Instant;
import java.util.UUID;

@EntityView(CustomCollection.class)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public interface CustomCollectionView {

    @IdMapping
    UUID getId();

    String getTitle();

    String getBodyHtml();

    String getHandle();

    Boolean getPublished();

    Instant getPublishedAt();

    String getPublishedScope();

    String getSortOrder();

    String getTemplateSuffix();

    Instant getCreatedAt();

    Instant getUpdatedAt();

    @Mapping("COUNT(collects)")
    Long getItemsCount();
    @Mapping("image")

    CollectionImageView getImage();

    @EntityView(CollectionImage.class)
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    interface CollectionImageView {
        String getSrc();
        Instant getCreatedAt();
    }
}