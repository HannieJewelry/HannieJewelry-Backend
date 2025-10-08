package hanniejewelry.vn.order.domain.model.enums;

import com.fasterxml.jackson.annotation.JsonValue;

public enum OrderStatus {
    OPEN,       // open: Show only open orders.
    CLOSED,     // closed: Show only closed orders.
    CANCELLED,  // cancelled: Show only canceled orders.
    ANY;         // any: Show orders of any status, including archived orders.

    @JsonValue
    public String toJson() {
        return name().toLowerCase();
    }
}
