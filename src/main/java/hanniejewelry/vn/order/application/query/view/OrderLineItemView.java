package hanniejewelry.vn.order.application.query.view;

import com.blazebit.persistence.view.*;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import hanniejewelry.vn.order.domain.model.entity.OrderLineItem;
import hanniejewelry.vn.order.domain.model.enums.LineItemFulfillmentStatus;
import java.math.BigDecimal;
import java.util.UUID;

@EntityView(OrderLineItem.class)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public interface OrderLineItemView {
    @IdMapping
    UUID getId();
    Integer getQuantity();
    Long getVariantId();
    UUID getProductId();
    BigDecimal getPrice();
    BigDecimal getPriceOriginal();
    BigDecimal getPricePromotion();
    BigDecimal getTotalDiscount();
    LineItemFulfillmentStatus getFulfillmentStatus();
    String getFulfillmentService();
    Integer getFulfillableQuantity();
    Boolean getRequiresShipping();
    String getSku();
    String getTitle();
    String getVariantTitle();
    String getVendor();
    String getType();
    String getName();
    Boolean getGiftCard();
    Boolean getTaxable();
    String getBarcode();
    Double getGrams();
    Boolean getProductExists();
    Boolean getNotAllowPromotion();
    BigDecimal getMaCostAmount();
    BigDecimal getActualPrice();
    String getImageSrc();
}
