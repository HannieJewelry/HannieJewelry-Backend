package hanniejewelry.vn.order.domain.model.enums;

import com.fasterxml.jackson.annotation.JsonValue;

public enum ClosedStatus {
    UNCLOSED,   // unclosed
    CLOSED;      // closed

    @JsonValue
    public String toJson() {
        return name().toLowerCase();
    }
} 