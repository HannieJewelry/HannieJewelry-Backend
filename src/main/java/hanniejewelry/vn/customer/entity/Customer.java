package hanniejewelry.vn.customer.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import hanniejewelry.vn.customer.enums.Gender;
import hanniejewelry.vn.order.entity.Order;
import hanniejewelry.vn.product.entity.SoftDeletable;
import hanniejewelry.vn.shared.domain.model.entity.AbstractAuditEntity;
import hanniejewelry.vn.user.entity.User;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.Where;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.*;

@Entity
@Table(name = "customers")
@Getter
@Setter
@SuperBuilder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@Where(clause = "is_deleted = false")
public class Customer extends AbstractAuditEntity implements SoftDeletable {

    @Id
    @GeneratedValue
    @Column(columnDefinition = "uuid")
    private UUID id;

    @Column(name = "accepts_marketing", nullable = false)
    @Builder.Default
    private Boolean acceptsMarketing = true;

    @Column(name = "email", unique = true)
    private String email;

    @Column(name = "phone")
    private String phone;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "avatar_url")
    private String avatarUrl;

    @Column(name = "last_order_id", columnDefinition = "uuid")
    private UUID lastOrderId;

    @Column(name = "last_order_name")
    private String lastOrderName;

    @Column(name = "note", columnDefinition = "TEXT")
    private String note;

    @Column(name = "orders_count", nullable = false)
    @Builder.Default
    private Integer ordersCount = 0;

    @Column(name = "tags")
    private String tags;

    @Column(name = "total_spent", precision = 16, scale = 1, nullable = false)
    @Builder.Default
    private BigDecimal totalSpent = BigDecimal.ZERO;

    @Column(name = "total_paid", precision = 16, scale = 1, nullable = false)
    @Builder.Default
    private BigDecimal totalPaid = BigDecimal.ZERO;

    @Column(name = "group_name")
    private String groupName;

    @Column(name = "birthday")
    private Instant birthday;

    @Enumerated(EnumType.ORDINAL)
    @Column(name = "gender")
    private Gender gender;

    @Column(name = "last_order_date")
    private Instant lastOrderDate;

    @OneToOne
    @JoinColumn(name = "user_id", unique = true)
    private User user;

    @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @Builder.Default
    private List<CustomerAddress> addresses = new ArrayList<>();

    @OneToOne(mappedBy = "customer", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private EInvoiceInfo eInvoiceInfo;
//    @Column(name = "is_guest", nullable = false)
//    @Builder.Default
//    private Boolean isGuest = false;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "customer_segment_mapping",
            joinColumns = @JoinColumn(name = "customer_id"),
            inverseJoinColumns = @JoinColumn(name = "segment_id")
    )
    @Builder.Default
    private Set<CustomerSegment> segments = new HashSet<>();
    
    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", referencedColumnName = "id", insertable = false, updatable = false)
    @Builder.Default
    @JsonManagedReference
    private List<Order> orders = new ArrayList<>();

    @Override
    public void setDeleted(boolean deleted) {
        this.isDeleted = deleted;
    }

    @Override
    public boolean isDeleted() {
        return Boolean.TRUE.equals(this.isDeleted);
    }

    @Transient
    public String getFullName() {
        if (firstName == null && lastName == null) {
            return null;
        }
        
        if (firstName == null) {
            return lastName;
        }
        
        if (lastName == null) {
            return firstName;
        }
        
        return firstName + " " + lastName;
    }
}
