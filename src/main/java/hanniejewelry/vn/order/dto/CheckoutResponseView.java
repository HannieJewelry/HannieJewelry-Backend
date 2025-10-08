package hanniejewelry.vn.order.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import hanniejewelry.vn.payment.dto.PaymentMethodView;
import hanniejewelry.vn.order.api.dto.LineItemDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.Map;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class CheckoutResponseView {
    private String id;
    private java.util.UUID addressId;
    private String phoneCountryCode;
    private Long shippingMethodId;
    private String shippingMethodName;
    private String paymentMethodName;
    private Long paymentMethodId;
    private Boolean payDepositOnly;
    private Boolean depositRequired;
    private Boolean eInvoiceRequest;
    
    private EInvoiceInfo eInvoiceInfo;
    private UtmParameters utmParameters;
    
    private BigDecimal depositAmount;
    private Integer depositCODAmount;
    private String note;
    private Boolean requiresShipping;
    private Instant createdAt;
    private Instant updatedAt;
    private Instant completedAt;
    
    private List<String> errors;
    private List<String> warnings;
    private List<String> userErrors;
    private Boolean ready;
    
    private List<LineItemDTO> lineItems;
    private List<ShippingMethodView> shippingMethods;
    private List<PaymentMethodView> paymentMethods;
    private List<DiscountAllocation> discountAllocations;
    private List<Object> giftCards;
    private Map<String, String> attributes;
    
    private BigDecimal discount;
    private Long locationId;
    private String locationName;
    private Long locationCountryId;
    private String authEmail;
    private String authPhone;
    private String authName;
    
    private CustomerMembership customerMembership;
    
    private BigDecimal subTotalBeforeTax;
    private BigDecimal subTotalTax;
    private BigDecimal subTotal;
    private BigDecimal shippingBeforeTax;
    private BigDecimal shippingTax;
    private BigDecimal shipping;
    private Integer totalTaxIncluded;
    private BigDecimal totalTaxNotIncluded;
    private BigDecimal total;
    
    private OrderView order;
    private String channel;
    private Boolean disallowLoyaltyProgram;
    private Boolean sendNotify;
    private Boolean sendReceipt;
    private Boolean allowPickAtLocation;
    private Boolean pickAtLocation;
    private Object nextActions;
    
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public static class EInvoiceInfo {
        private Boolean company;
        private String name;
        private String taxCode;
        private String address;
        private String email;
        private Boolean save;
        private Boolean submit;
    }
    
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public static class UtmParameters {
        private String source;
        private String medium;
        private String campaign;
        private String term;
        private String content;
        private String id;
        private String sourcePlatform;
        private String landingSite;
        private String landingSiteRef;
        private String referrerSite;
        private String trackingId;
    }
    
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public static class LineItemView {
        private Long id;
        private Integer seq;
        private Long variantId;
        private Integer variantType;
        private Long productId;
        private String productTitle;
        private String variantTitle;
        private String imageUrl;
        private BigDecimal priceOriginal;
        private BigDecimal price;
        private BigDecimal linePrice;
        private Integer lineDiscount;
        private BigDecimal quantity;
        private BigDecimal weight;
        private BigDecimal lineWeight;
        private BigDecimal lineTax;
        private BigDecimal lineAmount;
        private Integer availableQuantity;
        private Boolean lotSupport;
        private Long variantUnitId;
        private Integer unitRatio;
        private String unit;
        private Boolean requiresShipping;
        private Boolean notAllowPromotion;
        private List<DiscountAllocation> discountAllocations;
        private List<Object> attributes;
        private Object components;
    }
    
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public static class DiscountAllocation {
        private Long discountId;
        private String type;
        private String name;
        private BigDecimal amount;
        private Integer valueType;
        private Integer value;
        private Boolean canCombine;
    }
    
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public static class CustomerMembership {
        private String tier;
        private BigDecimal accumulateSpent;
        private String nextTier;
        private Integer rewardPoint;
        private Integer maxRewardPoint;
        private BigDecimal maxRewardAmount;
        private Integer earnRewardPoint;
        private String expireDate;
    }
    
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public static class OrderView {
        private Long id;
        private String orderName;
        private Instant orderDate;
        private String orderStatus;
        private String fullName;
        private String phoneCountryCode;
        private String phone;
        private String email;
        private String shippingAddress;
        private Long shippingWardId;
        private Long shippingDistrictId;
        private Long shippingProvinceId;
        private Long shippingCountryId;
        private String shippingCity;
        private String shippingZipCode;
        private String shippingMethod;
        private String shippingStatus;
        private String shippingTrackingCompany;
        private String shippingTrackingNumber;
        private String shippingTrackingUrl;
        private String paymentStatus;
        private Long paymentMethodId;
        private Integer paymentMethodType;
        private String paymentMethod;
        private String paymentQrData;
        private Boolean paymentQrSupportIpn;
        private String pendingPaymentTransactionId;
        private String pendingPaymentTransactionArtifact;
        private Boolean canRequestPay;
        private Boolean canUpdateEInvoice;
        private Boolean pickAtLocation;
        private BigDecimal payableAmount;
        private Boolean payDepositOnly;
        private Integer payableDepositAmount;
        private Integer codAmount;
        private BigDecimal subTotal;
        private BigDecimal subTotalTax;
        private BigDecimal shippingTax;
        private BigDecimal totalTaxIncluded;
        private BigDecimal totalTaxNotIncluded;
        private BigDecimal discount;
        private BigDecimal shipping;
        private BigDecimal total;
        private BigDecimal totalPaid;
        private List<LineItemDTO> lineItems;
        private List<DiscountAllocation> discountAllocations;
        private List<TimelineItem> timelines;
        private List<Object> attributes;
        private String note;
        private String channel;
        private String refOrderId;
        private LocationInfo location;
        private EInvoiceInfo eInvoiceInfo;
        private String authEmail;
        private String authPhone;
        private String authName;
        private CustomerMembership customerMembership;
    }
    
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public static class TimelineItem {
        private Instant createdAt;
        private String action;
    }
    
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public static class LocationInfo {
        private Long id;
        private String name;
        private String phone;
        private String email;
        private String address;
        private Long countryId;
        private Long provinceId;
        private Long districtId;
        private Long wardId;
        private String city;
        private String postalZipCode;
    }
} 