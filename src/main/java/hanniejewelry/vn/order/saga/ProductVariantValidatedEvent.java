package hanniejewelry.vn.order.saga;

import java.util.UUID;

public record ProductVariantValidatedEvent(UUID orderId, Long variantId, Integer quantity) {}
