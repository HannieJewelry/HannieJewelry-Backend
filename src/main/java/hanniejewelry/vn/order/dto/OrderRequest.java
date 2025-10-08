package hanniejewelry.vn.order.dto;

import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class OrderRequest {
    @Valid
    @NotNull(message = "Order information is required")
    private OrderInfo order;
    
    private Long paymentMethodId;

    @Data
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public static class OrderInfo {
        private AddressInfo billingAddress;
        private List<DiscountCode> discountCodes;
        @NotBlank(message = "Email is required")
        private String email;
        private String tags;
        private String gateway;
        private String landingSite;
        private String landingSiteRef;
        private String source;
        @NotEmpty(message = "Line items are required")
        private List<LineItem> lineItems;
        private String note;
        private String referringSite;
        @Valid
        private AddressInfo shippingAddress;
        private List<ShippingLine> shippingLines;
        private String sourceName;
        private List<NoteAttribute> noteAttributes;
        private UUID userId;
        private UUID locationId;
        private String utmSource;
        private String utmMedium;
        private String utmCampaign;
        private String utmTerm;
        private String utmContent;
        private String cartToken;
    }

    @Data
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public static class AddressInfo {
        private String address1;
        private String address2;
        private String company;
        private String firstName;
        private String lastName;
        private String phone;
        private String zip;
        private String provinceCode;
        private String countryCode;
        private String districtCode;
        private String wardCode;
    }

    @Data
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public static class DiscountCode {
        private String code;
        private BigDecimal amount;
        private String type;
    }

    @Data
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public static class LineItem {
        @NotNull(message = "Quantity is required")
        private Integer quantity;
        @NotNull(message = "Variant ID is required")
        private Long variantId;
    }

    @Data
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public static class ShippingLine {
        private String code;
        private BigDecimal price;
        private String source;
        private String title;
    }

    @Data
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public static class NoteAttribute {
        private String name;
        private String value;
    }
}
