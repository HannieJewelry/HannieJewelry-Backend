package hanniejewelry.vn.inventory.entity;

import hanniejewelry.vn.product.entity.SoftDeletable;
import hanniejewelry.vn.shared.domain.model.entity.AbstractAuditEntity;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.Where;

import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "locations")
@Getter
@Setter
@SuperBuilder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@Where(clause = "is_deleted = false")
public class Location extends AbstractAuditEntity implements SoftDeletable {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(columnDefinition = "uuid", updatable = false, nullable = false)
    private UUID id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String locationName;

    private String address;

    private String email;

    private String addressCont;

    private String phone;

    private String street;

    private String city;

    private Integer countryId;

    private String countryName;

    private Integer provinceId;

    private String provinceName;

    private String postalZipCode;

    private boolean isPrimaryLocation;

    private boolean isShippingLocation;

    private boolean isUnavailableQty;

    private Double latitude;

    private Double longitude;

    private Integer districtId;

    private Integer wardId;

    private String wardName;

    private String wardCode;

    private String addressDisplay;

    private String districtName;

    private boolean holdStock;

    private String flexShipHubCode;

    private String proShipHubCode;

    private String ghtkHubCode;

    private String ghnHubCode;

    private String ghNv3HubCode;

    private String shipChungHubCode;

    private String viettelPostHubCode;

    private String dhlHubCode;

    private String viettelPost2018HubCode;

    @ElementCollection
    @CollectionTable(name = "location_user_ids", joinColumns = @JoinColumn(name = "location_id"))
    @Column(name = "user_id")
    private List<Long> userIds;

    @ElementCollection
    @CollectionTable(name = "location_group_ids", joinColumns = @JoinColumn(name = "location_id"))
    @Column(name = "group_id")
    private List<Long> groupIds;

    private Integer typeId;

    private Integer areaId;

    private String areaName;

    private Integer status;
    
    @Override
    public void setDeleted(boolean deleted) {
        this.isDeleted = deleted;
    }

    @Override
    public boolean isDeleted() {
        return Boolean.TRUE.equals(this.isDeleted);
    }
} 