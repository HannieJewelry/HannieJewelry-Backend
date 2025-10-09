package hanniejewelry.vn.order.domain.model.enums;

import com.fasterxml.jackson.annotation.JsonValue;

public enum ProcessingMethod {
    CHECKOUT,  // checkout: The order was processed using the Haravan checkout.
    DIRECT,    // direct: The order was processed using a direct payment provider.
    MANUAL,    // manual: The order was processed using a manual payment method.
    OFFSITE;    // offsite: The order was processed by an external payment provider.

    @JsonValue
    public String toJson() {
        return name().toLowerCase();
    }
}
