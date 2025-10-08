package hanniejewelry.vn.order.domain.model.enums;

import com.fasterxml.jackson.annotation.JsonValue;

public enum CancelledStatus {
    UNCANCELLED, // uncancelled
    CANCELLED;    // cancelled

    @JsonValue
    public String toJson() {
        return name().toLowerCase();
    }
} 