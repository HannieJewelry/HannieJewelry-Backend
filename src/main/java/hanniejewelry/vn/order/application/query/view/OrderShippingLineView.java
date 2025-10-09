package hanniejewelry.vn.order.application.query.view;

import com.blazebit.persistence.view.*;
import hanniejewelry.vn.order.domain.model.entity.OrderShippingLine;
import java.math.BigDecimal;
import java.util.UUID;

@EntityView(OrderShippingLine.class)
public interface OrderShippingLineView {
    @IdMapping
    UUID getId();
    String getCode();
    BigDecimal getPrice();
    String getSource();
    String getTitle();
}
