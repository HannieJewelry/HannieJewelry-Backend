package hanniejewelry.vn.product.view;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.blazebit.persistence.view.EntityView;
import hanniejewelry.vn.product.entity.InventoryAdvance;

@EntityView(InventoryAdvance.class)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public interface InventoryAdvanceView {
    Integer getQtyAvailable();
    Integer getQtyOnhand();
    Integer getQtyCommited();
    Integer getQtyIncoming();
}
