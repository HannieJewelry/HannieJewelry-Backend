package hanniejewelry.vn.order.domain.model.enums;

import com.fasterxml.jackson.annotation.JsonValue;

public enum DiscountType {
    PERCENTAGE,     // percentage: % discount
    SHIPPING,       // shipping: Shipping discount
    FIXED_AMOUNT;    // fixed_amount: Amount discount (default)

    @JsonValue
    public String toJson() {
        return name().toLowerCase();
    }
}