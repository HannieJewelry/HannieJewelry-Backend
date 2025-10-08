package hanniejewelry.vn.order.application.query.view;

import com.blazebit.persistence.view.*;
import hanniejewelry.vn.shipping.infrastructure.persistence.entity.OrderBillingAddress;

import java.util.UUID;

@EntityView(OrderBillingAddress.class)
public interface OrderBillingAddressView {
    @IdMapping
    UUID getId();
    String getAddress1();
    String getAddress2();
    String getCity();
    String getCompany();
    String getCountry();
    String getFirstName();
    String getLastName();
    String getPhone();
    String getProvince();
    String getZip();
    String getName();
    String getProvinceCode();
    String getCountryCode();
    Boolean getDefaultAddress();
    String getDistrict();
    String getDistrictCode();
    String getWard();
    String getWardCode();
}
