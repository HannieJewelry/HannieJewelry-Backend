package hanniejewelry.vn.product.entity;

import com.blazebit.persistence.CTE;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@CTE
@Entity
@NoArgsConstructor
@Getter
@Setter
public final class FilteredItemCTE {

  @Id private Long id;
  private String name;
  private String description;
}
