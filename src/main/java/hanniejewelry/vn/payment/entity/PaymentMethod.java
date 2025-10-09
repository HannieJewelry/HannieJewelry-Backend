package hanniejewelry.vn.payment.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "payment_methods")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PaymentMethod {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column
    private String description;

    @Column
    private String code;

    @Column
    private Boolean active;

    @Column(name = "display_order")
    private Integer displayOrder;
    
    @Column
    private Boolean online;
    
    @Column(name = "system_type_id")
    private Integer systemTypeId;
} 