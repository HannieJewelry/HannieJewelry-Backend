package hanniejewelry.vn.order.application.query.view;

import com.blazebit.persistence.view.*;
import hanniejewelry.vn.shipping.infrastructure.persistence.entity.OrderShippingAddress;

import java.util.UUID;

@EntityView(OrderShippingAddress.class)
public interface OrderShippingAddressView {
    @IdMapping
    UUID getId();
    String getAddress1();
    String getAddress2();
    String getCity();
    String getCompany();
    String getCountry();
    String getFirstName();
    String getLastName();
    Double getLatitude();
    Double getLongitude();
    String getPhone();
    String getProvince();
    String getZip();
    String getName();
    String getProvinceCode();
    String getCountryCode();
    String getDistrict();
    String getDistrictCode();
    String getWard();
    String getWardCode();
}
