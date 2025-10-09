package hanniejewelry.vn.order.api.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class OrderResponse {
    private OrderDetails order;

    @Data
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public static class OrderDetails {
        private AddressInfo billingAddress;
        private String browserIp;
        private Boolean buyerAcceptsMarketing;
        private String cancelReason;
        private Instant cancelledAt;
        private String cartToken;
        private String checkoutToken;
        private ClientDetails clientDetails;
        private Instant closedAt;
        private Instant createdAt;
        private String currency;
        private Customer customer;
        private List<DiscountCode> discountCodes;
        private String email;
        private String financialStatus;
        private List<Fulfillment> fulfillments;
        private String fulfillmentStatus;
        private String tags;
        private String gateway;
        private String gatewayCode;
        private UUID id;
        private String landingSite;
        private String landingSiteRef;
        private String source;
        private List<LineItem> lineItems;
        private String name;
        private String note;
        private Long number;
        private String orderNumber;
        private String processingMethod;
        private String referringSite;
        private List<Refund> refunds;
        private AddressInfo shippingAddress;
        private List<ShippingLine> shippingLines;
        private String sourceName;
        private BigDecimal subtotalPrice;
        private List<TaxLine> taxLines;
        private Boolean taxesIncluded;
        private String token;
        private BigDecimal totalDiscounts;
        private BigDecimal totalLineItemsPrice;
        private BigDecimal totalPrice;
        private BigDecimal totalTax;
        private Double totalWeight;
        private Instant updatedAt;
        private List<Transaction> transactions;
        private List<NoteAttribute> noteAttributes;
        private Instant confirmedAt;
        private String closedStatus;
        private String cancelledStatus;
        private String confirmedStatus;
        private UUID userId;
        private String deviceId;
        private UUID locationId;
        private String locationName;
        private Long refOrderId;
        private String refOrderNumber;
        private Instant refOrderDate;
        private String confirmUser;
        private String utmSource;
        private String utmMedium;
        private String utmCampaign;
        private String utmTerm;
        private String utmContent;
        private String redeemModel;
        private String paymentUrl;
        private String contactEmail;
        private UUID assignedLocationId;
        private String assignedLocationName;
        private Instant assignedLocationAt;
        private Instant exportedConfirmAt;
        private String orderProcessingStatus;
        private UUID prevOrderId;
        private String prevOrderNumber;
        private Instant prevOrderDate;
        private String riskLevel;
        private String taxBehaviour;
        private List<DiscountApplication> discountApplications;
    }

    @Data
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public static class AddressInfo {
        private String address1;
        private String address2;
        private String city;
        private String company;
        private String country;
        private String firstName;
        private UUID id;
        private String lastName;
        private String phone;
        private String province;
        private String zip;
        private String name;
        private String provinceCode;
        private String countryCode;
        private Boolean defaultAddress;
        private String district;
        private String districtCode;
        private String ward;
        private String wardCode;
        private Double latitude;
        private Double longitude;
    }

    @Data
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public static class ClientDetails {
        private String acceptLanguage;
        private String browserIp;
        private String sessionHash;
        private String userAgent;
        private Integer browserHeight;
        private Integer browserWidth;
    }

    @Data
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public static class Customer {
        private Boolean acceptsMarketing;
        private List<AddressInfo> addresses;
        private Instant createdAt;
        private AddressInfo defaultAddress;
        private String email;
        private String phone;
        private String firstName;
        private UUID id;
        private String multipassIdentifier;
        private String lastName;
        private UUID lastOrderId;
        private String lastOrderName;
        private String note;
        private Integer ordersCount;
        private String state;
        private String tags;
        private BigDecimal totalSpent;
        private BigDecimal totalPaid;
        private Instant updatedAt;
        private Boolean verifiedEmail;
        private Boolean sendEmailInvite;
        private Boolean sendEmailWelcome;
        private String password;
        private String passwordConfirmation;
        private String groupName;
        private Instant birthday;
        private Integer gender;
        private Instant lastOrderDate;
    }

    @Data
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public static class DiscountCode {
        private BigDecimal amount;
        private String code;
        private String type;
        private Boolean isCouponCode;
    }

    @Data
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public static class Fulfillment {
    }

    @Data
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public static class LineItem {
        private Integer fulfillableQuantity;
        private String fulfillmentService;
        private String fulfillmentStatus;
        private Double grams;
        private UUID id;
        private BigDecimal price;
        private BigDecimal priceOriginal;
        private BigDecimal pricePromotion;
        private UUID productId;
        private Integer quantity;
        private Boolean requiresShipping;
        private String sku;
        private String title;
        private Long variantId;
        private String variantTitle;
        private String vendor;
        private String type;
        private String name;
        private Boolean giftCard;
        private Boolean taxable;
        private List<TaxLine> taxLines;
        private Boolean productExists;
        private String barcode;
        private List<Property> properties;
        private BigDecimal totalDiscount;
        private List<AppliedDiscount> appliedDiscounts;
        private ImageInfo image;
        private Boolean notAllowPromotion;
        private BigDecimal maCostAmount;
        private BigDecimal actualPrice;
        private List<DiscountAllocation> discountAllocations;
    }

    @Data
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public static class Property {
    }

    @Data
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public static class AppliedDiscount {
    }

    @Data
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public static class ImageInfo {
        private String src;
        private String attactment;
        private String filename;
    }

    @Data
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public static class DiscountAllocation {
    }

    @Data
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public static class Refund {
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
    public static class TaxLine {
    }

    @Data
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public static class Transaction {
        private BigDecimal amount;
        private String authorization;
        private Instant createdAt;
        private String deviceId;
        private String gateway;
        private UUID id;
        private String kind;
        private UUID orderId;
        private String receipt;
        private String status;
        private UUID userId;
        private UUID locationId;
        private String paymentDetails;
        private UUID parentId;
        private String currency;
        private String haravanTransactionId;
        private String externalTransactionId;
    }

    @Data
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public static class NoteAttribute {
        private String name;
        private String value;
    }

    @Data
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public static class DiscountApplication {
    }
}
