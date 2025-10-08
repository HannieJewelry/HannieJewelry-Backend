package hanniejewelry.vn.order.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import hanniejewelry.vn.customer.entity.Customer;
import hanniejewelry.vn.order.domain.model.entity.OrderLineItem;
import hanniejewelry.vn.order.domain.model.entity.OrderNoteAttribute;
import hanniejewelry.vn.order.domain.model.entity.OrderShippingLine;
import hanniejewelry.vn.order.domain.model.entity.OrderTransaction;
import hanniejewelry.vn.order.domain.model.enums.*;
import hanniejewelry.vn.promotion.domain.model.OrderDiscountCode;
import hanniejewelry.vn.shipping.infrastructure.persistence.entity.OrderBillingAddress;
import hanniejewelry.vn.shipping.infrastructure.persistence.entity.OrderShippingAddress;
import hanniejewelry.vn.shared.utils.SecurityUtils;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import hanniejewelry.vn.shared.domain.model.entity.AbstractAuditEntity;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "tb_orders")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder(toBuilder = true)
public class Order extends AbstractAuditEntity {

    @Id
    @Column(columnDefinition = "uuid", updatable = false, nullable = false)
    private UUID id;

    @Column(name = "order_code")
    private String orderCode;

    @Column(name = "order_number")
    private String orderNumber;

    @Column(name = "name")
    private String name;

    @Column(name = "email")
    private String email;


    @Column(name = "total_price", precision = 16, scale = 1)
    private BigDecimal totalPrice;

    @Column(name = "subtotal_price", precision = 16, scale = 1)
    private BigDecimal subtotalPrice;

    @Column(name = "total_discounts", precision = 16, scale = 1)
    private BigDecimal totalDiscounts;

    @Column(name = "total_line_items_price", precision = 16, scale = 1)
    private BigDecimal totalLineItemsPrice;

    @Column(name = "total_tax", precision = 16, scale = 1)
    private BigDecimal totalTax;

    @Column(name = "total_weight")
    private Double totalWeight;

    @Enumerated(EnumType.STRING)
    @Column(name = "financial_status")
    private FinancialStatus financialStatus;

    @Enumerated(EnumType.STRING)
    @Column(name = "fulfillment_status")
    private FulfillmentStatus fulfillmentStatus;

    @Enumerated(EnumType.STRING)
    @Column(name = "order_processing_status")
    private OrderProcessingStatus orderProcessingStatus;

    @Column(name = "tags")
    private String tags;

    @Column(name = "gateway")
    private String gateway;

    @Column(name = "gateway_code")
    private String gatewayCode;
    
    @Column(name = "payment_method_id")
    private Long paymentMethodId;
    
    @Column(name = "payment_method")
    private String paymentMethod;

    @Column(name = "note", columnDefinition = "TEXT")
    private String note;

    @Column(name = "source")
    private String source;

    @Column(name = "source_name")
    private String sourceName;

    @Column(name = "referring_site")
    private String referringSite;

    @Column(name = "landing_site")
    private String landingSite;

    @Column(name = "landing_site_ref")
    private String landingSiteRef;

    @Column(name = "closed_at")
    private Instant closedAt;

    @Column(name = "cancelled_at")
    private Instant cancelledAt;

    @Column(name = "cancelled_reason")
    private String cancelledReason;

    @Enumerated(EnumType.STRING)
    @Column(name = "closed_status")
    private ClosedStatus closedStatus;

    @Enumerated(EnumType.STRING)
    @Column(name = "cancelled_status")
    private CancelledStatus cancelledStatus;

    @Enumerated(EnumType.STRING)
    @Column(name = "confirmed_status")
    private ConfirmedStatus confirmedStatus;

    @Column(name = "confirmed_at")
    private Instant confirmedAt;

    @Column(name = "user_id")
    private UUID userId;

    @Column(name = "confirm_user")
    private String confirmUser;


    @Column(name = "device_id")
    private String deviceId;

    @Column(name = "location_id")
    private UUID locationId;

    @Column(name = "location_name")
    private String locationName;

    @Column(name = "contact_email")
    private String contactEmail;

    @Column(name = "utm_source")
    private String utmSource;

    @Column(name = "utm_medium")
    private String utmMedium;

    @Column(name = "utm_campaign")
    private String utmCampaign;

    @Column(name = "utm_term")
    private String utmTerm;

    @Column(name = "utm_content")
    private String utmContent;

    @Column(name = "is_deleted")
    @Builder.Default
    private Boolean isDeleted = false;
    
    @Column(name = "cart_token")
    private String cartToken;
    
    @Column(name = "checkout_token")
    private String checkoutToken;
    
    @Column(name = "token")
    private String token;
    
    @Column(name = "browser_ip")
    private String browserIp;
    
    @Column(name = "buyer_accepts_marketing")
    private Boolean buyerAcceptsMarketing;
    
    @Column(name = "currency")
    private String currency;
    
    @Column(name = "taxes_included")
    private Boolean taxesIncluded;
    
    @Column(name = "cancel_reason")
    private String cancelReason;
    
    @Column(name = "processing_method")
    private String processingMethod;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", referencedColumnName = "id", insertable = false, updatable = false)
    @JsonBackReference
    private Customer customer;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    @JsonManagedReference
    private List<OrderLineItem> lineItems = new ArrayList<>();

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    @JsonManagedReference
    private List<OrderShippingLine> shippingLines = new ArrayList<>();

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    @JsonManagedReference
    private List<OrderDiscountCode> discountCodes = new ArrayList<>();

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    @JsonManagedReference
    private List<OrderTransaction> transactions = new ArrayList<>();

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    @JsonManagedReference
    private List<OrderNoteAttribute> noteAttributes = new ArrayList<>();

    @OneToOne(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @JsonManagedReference
    private OrderShippingAddress shippingAddress;

    @OneToOne(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @JsonManagedReference
    private OrderBillingAddress billingAddress;
    
    /**
     * Pre-persist hook to ensure customer ID is set if user is authenticated
     */
    @PrePersist
    public void prePersist() {
        if (this.userId == null) {
            SecurityUtils.associateOrderWithCurrentCustomer(this);
        }
        
        if (this.createdAt == null) {
            this.createdAt = Instant.now();
        }
    }
    
    /**
     * Pre-update hook to ensure customer ID is set if user is authenticated
     */
    @PreUpdate
    public void preUpdate() {
        if (this.userId == null) {
            SecurityUtils.associateOrderWithCurrentCustomer(this);
        }
        
        this.updatedAt = Instant.now();
    }
}