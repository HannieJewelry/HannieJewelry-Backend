package hanniejewelry.vn.order.application.query.view;

import com.blazebit.persistence.view.*;
import hanniejewelry.vn.order.domain.model.entity.OrderNoteAttribute;
import java.util.UUID;

@EntityView(OrderNoteAttribute.class)
public interface OrderNoteAttributeView {
    @IdMapping
    UUID getId();
    String getName();
    String getValue();
}
