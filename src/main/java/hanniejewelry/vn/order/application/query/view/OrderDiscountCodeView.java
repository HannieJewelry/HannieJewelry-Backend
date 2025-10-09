package hanniejewelry.vn.order.application.query.view;

import com.blazebit.persistence.view.*;
import hanniejewelry.vn.promotion.domain.model.OrderDiscountCode;

import java.math.BigDecimal;
import java.util.UUID;

@EntityView(OrderDiscountCode.class)
public interface OrderDiscountCodeView {
    @IdMapping
    UUID getId();
    BigDecimal getAmount();
    String getCode();
    String getType();
    Boolean getIsCouponCode();
}
