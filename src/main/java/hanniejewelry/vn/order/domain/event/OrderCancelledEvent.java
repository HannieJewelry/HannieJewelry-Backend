package hanniejewelry.vn.order.domain.event;

import java.util.UUID;

public record OrderCancelledEvent(UUID orderId, String reason, String note) {} 