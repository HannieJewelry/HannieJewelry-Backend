package hanniejewelry.vn.order.domain.command;

import java.util.List;
import java.util.UUID;

public record CheckInventoryCommand(
        UUID orderId,
        List<Long> variantIds
) {}
