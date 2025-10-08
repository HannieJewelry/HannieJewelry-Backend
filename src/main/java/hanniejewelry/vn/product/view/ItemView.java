package hanniejewelry.vn.product.view;

import com.blazebit.persistence.view.AttributeFilter;
import com.blazebit.persistence.view.AttributeFilters;
import com.blazebit.persistence.view.EntityView;
import com.blazebit.persistence.view.IdMapping;
import com.blazebit.persistence.view.filter.*;
import hanniejewelry.vn.product.entity.Item;

import java.util.UUID;

@EntityView(Item.class)
public interface ItemView {
  @IdMapping
  UUID getId();

  @AttributeFilters({
          @AttributeFilter(value = EqualFilter.class, name = "eq"),
          @AttributeFilter(value = ContainsIgnoreCaseFilter.class, name = "like"),
          @AttributeFilter(value = StartsWithFilter.class, name = "start"),
          @AttributeFilter(value = EndsWithFilter.class, name = "end")
  })
  String getName();

  @AttributeFilters({
          @AttributeFilter(value = EqualFilter.class, name = "eq"),
          @AttributeFilter(value = ContainsIgnoreCaseFilter.class, name = "like"),
          @AttributeFilter(value = StartsWithFilter.class, name = "start"),
          @AttributeFilter(value = EndsWithFilter.class, name = "end")
  })
  String getDescription();

}
