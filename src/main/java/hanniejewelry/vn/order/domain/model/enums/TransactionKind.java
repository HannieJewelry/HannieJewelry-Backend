package hanniejewelry.vn.order.domain.model.enums;

import com.fasterxml.jackson.annotation.JsonValue;

public enum TransactionKind {
    PENDING,
    AUTHORIZATION,
    CAPTURE,
    SALE,
    VOID,
    REFUND;

    @JsonValue
    public String toJson() {
        return name().toLowerCase();
    }
}