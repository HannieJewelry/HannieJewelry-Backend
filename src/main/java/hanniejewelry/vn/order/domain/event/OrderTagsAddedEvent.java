package hanniejewelry.vn.order.domain.event;

import java.util.UUID;

public record OrderTagsAddedEvent(UUID orderId, String tags) {} 