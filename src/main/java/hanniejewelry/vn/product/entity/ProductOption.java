package hanniejewelry.vn.product.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import hanniejewelry.vn.shared.domain.model.entity.AbstractAuditEntity;

import java.util.UUID;

@Entity
@Table(name = "product_option")
@Getter @Setter @SuperBuilder(toBuilder = true)
@NoArgsConstructor @AllArgsConstructor
public class ProductOption extends AbstractAuditEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    private String name;
    private Integer position;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private Product product;
}
