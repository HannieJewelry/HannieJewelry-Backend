package hanniejewelry.vn.shopping_cart.entity;

import hanniejewelry.vn.product.entity.Product;
import hanniejewelry.vn.product.entity.ProductVariant;
import hanniejewelry.vn.product.entity.SoftDeletable;
import hanniejewelry.vn.shared.domain.model.entity.AbstractAuditEntity;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.Where;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Entity
@Table(name = "cart_items")
@Getter
@Setter
@SuperBuilder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@Where(clause = "is_deleted = false")
public class CartItem extends AbstractAuditEntity implements SoftDeletable {

    @Id
    @GeneratedValue
    @Column(columnDefinition = "uuid")
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cart_id", nullable = false)
    private ShoppingCart cart;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "variant_id", nullable = false)
    private ProductVariant variant;
    
    @Column(name = "variant_id", insertable = false, updatable = false)
    private Long variantIdValue;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "variant_title")
    private String variantTitle;

    @Column(name = "price", precision = 16, scale = 1, nullable = false)
    private BigDecimal price;

    @Column(name = "line_price", precision = 16, scale = 1, nullable = false)
    private BigDecimal linePrice;

    @Column(name = "price_original", precision = 16, scale = 1, nullable = false)
    private BigDecimal priceOriginal;

    @Column(name = "line_price_original", precision = 16, scale = 1, nullable = false)
    private BigDecimal linePriceOriginal;

    @Column(name = "quantity", nullable = false)
    private Integer quantity;

    @Column(name = "sku")
    private String sku;

    @Column(name = "barcode")
    private String barcode;

    @Column(name = "grams", precision = 16, scale = 1)
    private BigDecimal grams;

    @Column(name = "line_weight", precision = 16, scale = 1)
    private BigDecimal lineWeight;

    @Column(name = "product_type")
    private String productType;

    @Column(name = "vendor")
    private String vendor;

    @Column(name = "image")
    private String image;

    @Column(name = "handle")
    private String handle;

    @Column(name = "requires_shipping")
    private Boolean requiresShipping;

    @Column(name = "gift_card")
    private Boolean giftCard;

    @Column(name = "not_allow_promotion")
    private Boolean notAllowPromotion;

    @ElementCollection
    @CollectionTable(name = "cart_item_properties", 
                    joinColumns = @JoinColumn(name = "cart_item_id"))
    @MapKeyColumn(name = "property_key")
    @Column(name = "property_value")
    @Builder.Default
    private Map<String, String> properties = new HashMap<>();

    @Override
    public void setDeleted(boolean deleted) {
        this.isDeleted = deleted;
    }

    @Override
    public boolean isDeleted() {
        return Boolean.TRUE.equals(this.isDeleted);
    }

    public void updateQuantity(Integer newQuantity) {
        this.quantity = newQuantity;
        this.linePrice = this.price.multiply(BigDecimal.valueOf(newQuantity));
        this.linePriceOriginal = this.priceOriginal.multiply(BigDecimal.valueOf(newQuantity));
        this.lineWeight = this.grams.multiply(BigDecimal.valueOf(newQuantity));
    }
} 