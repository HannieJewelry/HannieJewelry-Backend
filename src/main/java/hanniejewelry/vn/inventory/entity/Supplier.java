package hanniejewelry.vn.inventory.entity;

import hanniejewelry.vn.product.entity.SoftDeletable;
import hanniejewelry.vn.shared.domain.model.entity.AbstractAuditEntity;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.Where;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "suppliers")
@Getter
@Setter
@SuperBuilder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@Where(clause = "is_deleted = false")
public class Supplier extends AbstractAuditEntity implements SoftDeletable {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(columnDefinition = "uuid", updatable = false, nullable = false)
    private UUID id;

    private String firstName;
    
    private String lastName;
    
    @Column(nullable = false)
    private String supplierName;
    
    private String phone;
    
    private String email;
    
    private Integer status;
    
    private String zipCode;
    
    private Integer countryId;
    
    private String location;
    
    private String address;
    
    private Integer provinceId;
    
    private Integer districtId;
    
    private String city;
    
    private String countryName;
    
    private String provinceName;
    
    private String districtName;
    
    private Integer wardId;
    
    private String wardName;
    
    private Instant createdDate;
    
    private String createdName;
    
    @Column(precision = 19, scale = 4)
    private BigDecimal debt;
    
    @Column(precision = 19, scale = 4)
    private BigDecimal totalPurchase;
    
    private String company;
    
    private String taxCode;
    
    private String notes;
    
    private String tags;
    
    @Override
    public void setDeleted(boolean deleted) {
        this.isDeleted = deleted;
    }

    @Override
    public boolean isDeleted() {
        return Boolean.TRUE.equals(this.isDeleted);
    }
} 