package hanniejewelry.vn.product.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import hanniejewelry.vn.shared.domain.model.entity.AbstractAuditEntity;

import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Table(name = "product_variant")
@Getter
@Setter
@SuperBuilder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class ProductVariant extends AbstractAuditEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    @Column(nullable = false, precision = 16, scale = 1)
    @Builder.Default

    private BigDecimal price = BigDecimal.ZERO;
    @Column(name = "compare_at_price", precision = 16, scale = 1)
    @Builder.Default
    private BigDecimal compareAtPrice = BigDecimal.ZERO;

    @Column(name = "cost_price", precision = 16, scale = 1)
    @Builder.Default
    private BigDecimal costPrice = BigDecimal.ZERO;

    private String sku;
    private String barcode;

    @Column(nullable = false, precision = 10, scale = 1)
    @Builder.Default
    private BigDecimal grams = BigDecimal.ZERO;


    @Column(nullable = false)
    private Integer position;

    @Column(name = "inventory_quantity")
    @Builder.Default
    private Integer inventoryQuantity = 0;

    @Column(name = "inventory_policy")
    private String inventoryPolicy;
    @Column(name = "inventory_management")
    private String inventoryManagement;

    @Column(name = "is_tracking_inventory")
    @Builder.Default
    private boolean isTrackingInventory = false;

    @Column(name = "allow_purchase_when_sold_out")
    @Builder.Default
    private boolean allowPurchaseWhenSoldOut = false;

    @Column(nullable = false)
    @Builder.Default
    private boolean requiresShipping = false;

    @Column(nullable = false)
    @Builder.Default
    private boolean taxable = false;


    private String option1;
    private String option2;
    private String option3;
    @Column(name = "image_id")
    private UUID imageId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "image_id", insertable = false, updatable = false)
    private ProductImage image;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private Product product;

    @Embedded
    private InventoryAdvance inventoryAdvance;

}
