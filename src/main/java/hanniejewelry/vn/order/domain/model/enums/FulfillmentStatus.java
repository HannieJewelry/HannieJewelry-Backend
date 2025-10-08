package hanniejewelry.vn.order.domain.model.enums;

import com.fasterxml.jackson.annotation.JsonValue;

public enum FulfillmentStatus {
    FULFILLED,     // fulfilled: Every line item in the order has been fulfilled.
    PARTIAL,       // partial: At least one line item in the order has been fulfilled.
    RESTOCKED,     // restocked: All items have been restocked and order canceled.
    UNFULFILLED;    // unfulfilled: None of the line items have been fulfilled. (Dùng thay cho null)

    @JsonValue
    public String toJson() {
        return name().toLowerCase();
    }
}