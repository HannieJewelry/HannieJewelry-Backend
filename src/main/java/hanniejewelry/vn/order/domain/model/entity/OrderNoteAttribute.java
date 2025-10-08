package hanniejewelry.vn.order.domain.model.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import hanniejewelry.vn.order.entity.Order;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import hanniejewelry.vn.shared.domain.model.entity.AbstractAuditEntity;

import java.util.UUID;

@Entity
@Table(name = "tb_order_note_attributes")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder(toBuilder = true)
public class OrderNoteAttribute extends AbstractAuditEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(columnDefinition = "uuid", updatable = false, nullable = false)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false)
    @JsonBackReference
    private Order order;

    private String name;

    private String value;
} 