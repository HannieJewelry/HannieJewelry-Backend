package hanniejewelry.vn.product.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import hanniejewelry.vn.shared.domain.model.entity.AbstractAuditEntity;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "product_image")
@Getter @Setter @SuperBuilder(toBuilder = true)
@NoArgsConstructor @AllArgsConstructor
public class ProductImage extends AbstractAuditEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    private String src;
    private String filename;
    private Integer position;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "product_image_variant_ids", joinColumns = @JoinColumn(name = "product_image_id"))
    @Column(name = "variant_id")
    private Set<Long> variantIds = new HashSet<>(); // Đổi sang Set<Long>

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private Product product;
}
