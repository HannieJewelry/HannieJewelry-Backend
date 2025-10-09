package hanniejewelry.vn.order.domain.event;

import hanniejewelry.vn.order.dto.OrderRequest;

import java.util.UUID;

public record OrderUpdatedEvent(
    UUID orderId, 
    OrderRequest orderRequest
) {}