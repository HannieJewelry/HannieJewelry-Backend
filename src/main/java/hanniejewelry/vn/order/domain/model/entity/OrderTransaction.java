package hanniejewelry.vn.order.domain.model.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import hanniejewelry.vn.order.entity.Order;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import hanniejewelry.vn.shared.domain.model.entity.AbstractAuditEntity;

import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Table(name = "tb_order_transactions")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder(toBuilder = true)
public class OrderTransaction extends AbstractAuditEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(columnDefinition = "uuid", updatable = false, nullable = false)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false)
    @JsonBackReference
    private Order order;

    private BigDecimal amount;

    @Column(name = "auth_code")
    private String authCode;

    @Column(name = "created_user")
    private UUID createdUser;

    @Column(name = "payment_method_id")
    private UUID paymentMethodId;

    @Column(name = "transaction_type_id")
    private Integer transactionTypeId;

    @Column(name = "client_id")
    private String clientId;

    @Column(name = "device_id")
    private String deviceId;

    private String gateway;

    private String kind;

    private String receipt;

    private String status;

    private Boolean test;

    @Column(name = "user_id")
    private UUID userId;

    @Column(name = "location_id")
    private UUID locationId;

    @Column(name = "payment_details")
    private String paymentDetails;

    @Column(name = "parent_id")
    private UUID parentId;

    private String currency;

    @Column(name = "haravan_transaction_id")
    private String haravanTransactionId;

    @Column(name = "external_transaction_id")
    private String externalTransactionId;

    @Column(name = "send_email")
    private Boolean sendEmail;
} 