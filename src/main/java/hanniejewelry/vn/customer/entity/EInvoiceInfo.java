package hanniejewelry.vn.customer.entity;

import hanniejewelry.vn.shared.domain.model.entity.AbstractAuditEntity;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.UUID;

@Entity
@Table(name = "customer_einvoice_info")
@Getter
@Setter
@SuperBuilder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class EInvoiceInfo extends AbstractAuditEntity {

    @Id
    @GeneratedValue
    @Column(columnDefinition = "uuid")
    private UUID id;

    @Column(name = "name")
    private String name;

    @Column(name = "tax_code")
    private String taxCode;

    @Column(name = "email")
    private String email;

    @Column(name = "address", columnDefinition = "TEXT")
    private String address;

    @Column(name = "is_company", nullable = false)
    @Builder.Default
    private Boolean isCompany = false;

    @OneToOne
    @JoinColumn(name = "customer_id")
    private Customer customer;
} 