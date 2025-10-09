package hanniejewelry.vn.shopping_cart.entity;

import hanniejewelry.vn.customer.entity.Customer;
import hanniejewelry.vn.product.entity.SoftDeletable;
import hanniejewelry.vn.shared.domain.model.entity.AbstractAuditEntity;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.Where;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Entity
@Table(name = "shopping_carts")
@Getter
@Setter
@SuperBuilder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@Where(clause = "is_deleted = false")
public class ShoppingCart extends AbstractAuditEntity implements SoftDeletable {

    @Id
    @GeneratedValue
    @Column(columnDefinition = "uuid")
    private UUID id;

    @Column(name = "token", unique = true, nullable = false)
    private String token;

    @Column(name = "item_count")
    private Integer itemCount;

    @Column(name = "total_price", precision = 16, scale = 1)
    private BigDecimal totalPrice;

    @Column(name = "total_weight", precision = 16, scale = 1)
    private BigDecimal totalWeight;

    @Column(name = "note", columnDefinition = "TEXT")
    private String note;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id")
    private Customer customer;

    @Column(name = "location_id", columnDefinition = "uuid")
    private UUID locationId;

    @Column(name = "requires_shipping")
    private Boolean requiresShipping;

    @OneToMany(mappedBy = "cart", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<CartItem> items = new ArrayList<>();

    @ElementCollection
    @CollectionTable(name = "cart_attributes", 
                    joinColumns = @JoinColumn(name = "cart_id"))
    @MapKeyColumn(name = "attribute_key")
    @Column(name = "attribute_value")
    @Builder.Default
    private Map<String, String> attributes = new HashMap<>();

    @Override
    public void setDeleted(boolean deleted) {
        this.isDeleted = deleted;
    }

    @Override
    public boolean isDeleted() {
        return Boolean.TRUE.equals(this.isDeleted);
    }

    public void addItem(CartItem item) {
        items.add(item);
        item.setCart(this);
        recalculateTotals();
    }

    public void removeItem(CartItem item) {
        items.remove(item);
        item.setCart(null);
        recalculateTotals();
    }

    public void recalculateTotals() {
        this.itemCount = items.size();
        this.totalPrice = items.stream()
                .map(CartItem::getLinePrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        this.totalWeight = items.stream()
                .map(CartItem::getLineWeight)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        this.requiresShipping = items.stream()
                .anyMatch(CartItem::getRequiresShipping);
    }
} 