package hanniejewelry.vn.product.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import hanniejewelry.vn.shared.domain.model.entity.AbstractAuditEntity;

@Entity
@Table(name = "product_option_value")
@Getter @Setter @SuperBuilder(toBuilder = true)
@NoArgsConstructor @AllArgsConstructor
public class ProductOptionValue extends AbstractAuditEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String value;
    
    @Column(nullable = false)
    private String optionType;  // "size" or "color"
    
    @Column(nullable = false)
    private Integer position;
    
    @Column(nullable = false)
    @Builder.Default
    private boolean active = true;
    
    @Column(name = "color_code")
    private String colorCode;  // For color values only
} 