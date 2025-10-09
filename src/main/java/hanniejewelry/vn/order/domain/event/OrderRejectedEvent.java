package hanniejewelry.vn.order.domain.event;

public record OrderRejectedEvent(
        String orderId,
        String reason
) {}
