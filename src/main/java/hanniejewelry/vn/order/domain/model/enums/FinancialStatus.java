package hanniejewelry.vn.order.domain.model.enums;

import com.fasterxml.jackson.annotation.JsonValue;

public enum FinancialStatus {
    PENDING,             // pending: The payments are pending.
    AUTHORIZED,          // authorized: The payments have been authorized.
    PARTIALLY_PAID,      // partially_paid: The order has been partially paid.
    PAID,                // paid: The payments have been paid.
    PARTIALLY_REFUNDED,  // partially_refunded: The payments have been partially refunded.
    REFUNDED,            // refunded: The payments have been refunded.
    VOIDED;               // voided: The payments have been voided.

    @JsonValue
    public String toJson() {
        return name().toLowerCase();
    }
} 