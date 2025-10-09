package hanniejewelry.vn.order.domain.event;

import hanniejewelry.vn.order.dto.OrderRequest;
import java.util.UUID;

public record OrderSagaStartedEvent(
        UUID orderId,
        OrderRequest orderRequest
) {}
