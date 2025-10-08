package hanniejewelry.vn.order.domain.event;

import java.util.UUID;

public record OrderTagsDeletedEvent(UUID orderId, String tags) {} 