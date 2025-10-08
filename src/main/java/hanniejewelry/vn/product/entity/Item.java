package hanniejewelry.vn.product.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.Where;

import java.io.Serial;

@Entity
@Table(name = "items")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder(toBuilder = true)
@Where(clause = "is_deleted = false")
public final class Item extends BaseEntity implements SoftDeletable {

  @Serial private static final long serialVersionUID = 1L;

  @Column(nullable = false, length = 100)
  private String name;

  @Column(length = 500)
  private String description;


}
