package hanniejewelry.vn.order.domain.model.enums;

import com.fasterxml.jackson.annotation.JsonValue;

public enum ConfirmedStatus {
        UNCONFIRMED, // unconfirmed
        CONFIRMED;    // confirmed


    @JsonValue
    public String toJson() {
        return name().toLowerCase();
    }
} 