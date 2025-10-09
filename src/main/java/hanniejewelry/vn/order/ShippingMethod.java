package hanniejewelry.vn.order;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(name = "shipping_methods")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ShippingMethod {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column
    private String description;

    @Column(precision = 16, scale = 1)
    private BigDecimal price;

    @Column
    private String code;

    @Column
    private Boolean active;

    @Column(name = "display_order")
    private Integer displayOrder;
} 