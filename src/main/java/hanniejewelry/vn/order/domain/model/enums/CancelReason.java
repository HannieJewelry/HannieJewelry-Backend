package hanniejewelry.vn.order.domain.model.enums;

import com.fasterxml.jackson.annotation.JsonValue;

public enum CancelReason {
    CUSTOMER,    // customer: The customer changed or cancelled the order.
    FRAUD,       // fraud: The order was fraudulent.
    INVENTORY,   // inventory: Items in the order were not in inventory.
    DECLINED,    // declined: The payment was declined.
    OTHER;        // other: The order was cancelled for a reason not in the list above.

    @JsonValue
    public String toJson() {
        return name().toLowerCase();
    }
}
