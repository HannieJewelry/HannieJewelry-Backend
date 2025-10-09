package hanniejewelry.vn.payment.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "tb_transactions")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private String gateway;

    private Instant transactionDate;

    private String accountNumber;

    private String subAccount;

    private String code;

    @Column(columnDefinition = "TEXT")
    private String content;

    private String transferType;

    @Column(columnDefinition = "TEXT")
    private String description;

    private BigDecimal transferAmount;

    private String referenceCode;

    private BigDecimal accumulated;

    @PrePersist
    public void prePersist() {
        if (transactionDate == null) {
            transactionDate = Instant.now();
        }
    }
}
