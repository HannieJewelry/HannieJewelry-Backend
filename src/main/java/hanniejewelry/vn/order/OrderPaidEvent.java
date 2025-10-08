package hanniejewelry.vn.order;

import java.util.UUID;

public record OrderPaidEvent(
        UUID orderId
) {}

