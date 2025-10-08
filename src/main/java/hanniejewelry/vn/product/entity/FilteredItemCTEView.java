package hanniejewelry.vn.product.entity;

import com.blazebit.persistence.view.EntityView;
import com.blazebit.persistence.view.IdMapping;

@EntityView(FilteredItemCTE.class)
public interface FilteredItemCTEView {
  @IdMapping
  Long getId();

  String getName();

  String getDescription();
}
