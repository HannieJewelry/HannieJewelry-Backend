package hanniejewelry.vn.promotion.domain.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import hanniejewelry.vn.order.entity.Order;
import hanniejewelry.vn.shared.domain.model.entity.AbstractAuditEntity;

import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Table(name = "tb_order_discount_codes")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder(toBuilder = true)
public class OrderDiscountCode extends AbstractAuditEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(columnDefinition = "uuid", updatable = false, nullable = false)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false)
    @JsonBackReference
    private Order order;

    private BigDecimal amount;

    private String code;

    private String type;

    @Column(name = "is_coupon_code")
    private Boolean isCouponCode;
} 