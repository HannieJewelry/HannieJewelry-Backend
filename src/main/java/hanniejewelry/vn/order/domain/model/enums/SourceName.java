package hanniejewelry.vn.order.domain.model.enums;

import com.fasterxml.jackson.annotation.JsonValue;

public enum SourceName {
    WEB,                // web
    POS,                // pos
    HARAVAN_DRAFT_ORDER,// haravan_draft_order
    IPHONE,             // iphone
    ANDROID,            // android
    API;                 // Orders created via API, any string

    @JsonValue
    public String toJson() {
        return name().toLowerCase();
    }
}
