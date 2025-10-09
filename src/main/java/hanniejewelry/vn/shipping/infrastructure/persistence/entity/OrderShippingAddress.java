package hanniejewelry.vn.shipping.infrastructure.persistence.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import hanniejewelry.vn.order.entity.Order;
import hanniejewelry.vn.shared.domain.model.entity.AbstractAuditEntity;

import java.util.UUID;

@Entity
@Table(name = "tb_order_shipping_addresses")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder(toBuilder = true)
public class OrderShippingAddress extends AbstractAuditEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(columnDefinition = "uuid", updatable = false, nullable = false)
    private UUID id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false)
    @JsonBackReference
    private Order order;

    @Column(name = "address1")
    private String address1;

    @Column(name = "address2")
    private String address2;

    private String city;

    private String company;

    private String country;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    private Double latitude;

    private Double longitude;

    private String phone;

    private String province;

    private String zip;

    private String name;

    @Column(name = "province_code")
    private String provinceCode;

    @Column(name = "country_code")
    private String countryCode;

    private String district;

    @Column(name = "district_code")
    private String districtCode;

    private String ward;

    @Column(name = "ward_code")
    private String wardCode;
} 