package hanniejewelry.vn.order.saga;

import java.util.UUID;

public record ProductVariantInvalidEvent(UUID orderId, Long variantId, String reason) {}
