package hanniejewelry.vn.order.domain.model.enums;

import com.fasterxml.jackson.annotation.JsonValue;

public enum LineItemFulfillmentStatus {
    UNFULFILLED,
    PARTIALLY_FULFILLED,
    FULFILLED,
    NOT_FULFILLED;

    @JsonValue
    public String toJson() {
        return name().toLowerCase();
    }
} 