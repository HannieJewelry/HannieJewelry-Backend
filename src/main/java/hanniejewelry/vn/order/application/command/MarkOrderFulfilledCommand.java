package hanniejewelry.vn.order.application.command;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MarkOrderFulfilledCommand {
    private UUID orderId;
} 