package hanniejewelry.vn.order.domain.command;

import java.util.List;

public record CheckPromotionCommand(
        String orderId,
        List<String> promoCodes
) {}
