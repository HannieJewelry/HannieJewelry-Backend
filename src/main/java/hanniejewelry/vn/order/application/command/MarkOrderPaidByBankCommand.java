package hanniejewelry.vn.order.application.command;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MarkOrderPaidByBankCommand {
    private UUID orderId;
    private BigDecimal amount;
    private String gateway;
    private String referenceCode;
    private String paymentDetails;
} 