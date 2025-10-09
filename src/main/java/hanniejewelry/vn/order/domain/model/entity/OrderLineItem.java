package hanniejewelry.vn.order.domain.model.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import hanniejewelry.vn.order.entity.Order;
import hanniejewelry.vn.order.domain.model.enums.LineItemFulfillmentStatus;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import hanniejewelry.vn.shared.domain.model.entity.AbstractAuditEntity;

import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Table(name = "tb_order_line_items")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder(toBuilder = true)
public class OrderLineItem extends AbstractAuditEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(columnDefinition = "uuid", updatable = false, nullable = false)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false)
    @JsonBackReference
    private Order order;

    @Column(name = "quantity", nullable = false)
    private Integer quantity;

    @Column(name = "variant_id")
    private Long variantId;

    @Column(name = "product_id", columnDefinition = "uuid")
    private UUID productId;

    @Column(name = "price", precision = 16, scale = 1)
    private BigDecimal price;

    @Column(name = "price_original", precision = 16, scale = 1)
    private BigDecimal priceOriginal;

    @Column(name = "price_promotion", precision = 16, scale = 1)
    private BigDecimal pricePromotion;

    @Column(name = "total_discount", precision = 16, scale = 1)
    private BigDecimal totalDiscount;

    @Enumerated(EnumType.STRING)
    @Column(name = "fulfillment_status")
    private LineItemFulfillmentStatus fulfillmentStatus;

    @Column(name = "fulfillment_service")
    private String fulfillmentService;

    @Column(name = "fulfillable_quantity")
    private Integer fulfillableQuantity;

    @Column(name = "requires_shipping")
    private Boolean requiresShipping;

    @Column(name = "sku")
    private String sku;

    @Column(name = "title")
    private String title;

    @Column(name = "variant_title")
    private String variantTitle;

    @Column(name = "vendor")
    private String vendor;

    @Column(name = "type")
    private String type;

    @Column(name = "name")
    private String name;

    @Column(name = "gift_card")
    private Boolean giftCard;

    @Column(name = "taxable")
    private Boolean taxable;

    @Column(name = "barcode")
    private String barcode;

    @Column(name = "grams")
    private Double grams;
    
    @Column(name = "product_exists")
    private Boolean productExists;
    
    @Column(name = "not_allow_promotion")
    private Boolean notAllowPromotion;
    
    @Column(name = "ma_cost_amount", precision = 16, scale = 1)
    private BigDecimal maCostAmount;
    
    @Column(name = "actual_price", precision = 16, scale = 1)
    private BigDecimal actualPrice;
    
    @Column(name = "image_src")
    private String imageSrc;
} 