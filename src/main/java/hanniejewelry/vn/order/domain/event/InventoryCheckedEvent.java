package hanniejewelry.vn.order.domain.event;

import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Data;

public record InventoryCheckedEvent(UUID orderId, boolean valid) { }