package hanniejewelry.vn.order.domain.command;

import java.util.UUID;

public record RejectOrderCommand(
        UUID orderId,
        String reason
) {}
