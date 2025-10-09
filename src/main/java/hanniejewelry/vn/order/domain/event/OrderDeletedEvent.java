package hanniejewelry.vn.order.domain.event;

import java.util.UUID;

public record OrderDeletedEvent(UUID orderId) {}