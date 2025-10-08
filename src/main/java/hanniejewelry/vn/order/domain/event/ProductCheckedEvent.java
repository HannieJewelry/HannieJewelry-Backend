package hanniejewelry.vn.order.domain.event;

import java.util.UUID;

public record ProductCheckedEvent(UUID orderId, boolean valid) { }