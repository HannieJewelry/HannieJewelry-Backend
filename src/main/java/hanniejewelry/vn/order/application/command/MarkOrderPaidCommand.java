package hanniejewelry.vn.order.application.command;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.axonframework.modelling.command.TargetAggregateIdentifier;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MarkOrderPaidCommand {
    
    @TargetAggregateIdentifier
    private UUID orderId;
    
    private String orderCode;
    
    private BigDecimal amount;
}