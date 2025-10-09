package hanniejewelry.vn.order.application.query;

import lombok.Value;

import java.util.UUID;

public record FindOrderByIdQuery(UUID orderId) { }