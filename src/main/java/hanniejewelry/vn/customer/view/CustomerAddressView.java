package hanniejewelry.vn.customer.view;

import com.blazebit.persistence.view.AttributeFilter;
import com.blazebit.persistence.view.AttributeFilters;
import com.blazebit.persistence.view.EntityView;
import com.blazebit.persistence.view.IdMapping;
import com.blazebit.persistence.view.filter.*;
import com.fasterxml.jackson.annotation.JsonProperty;
import hanniejewelry.vn.customer.entity.CustomerAddress;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.UUID;

@EntityView(CustomerAddress.class)
@Schema(description = "Thông tin chi tiết địa chỉ khách hàng")
public interface CustomerAddressView {

    @IdMapping
    @JsonProperty("id")
    @Schema(description = "ID của địa chỉ", example = "123e4567-e89b-12d3-a456-426614174000")
    UUID getId();

    @AttributeFilters({
            @AttributeFilter(value = ContainsIgnoreCaseFilter.class, name = "like")
    })
    @JsonProperty("address1")
    @Schema(description = "Địa chỉ dòng 1", example = "123 Đường Nguyễn Huệ")
    String getAddress1();

    @JsonProperty("address2")
    @Schema(description = "Địa chỉ dòng 2", example = "Phường Bến Nghé")
    String getAddress2();

    @AttributeFilters({
            @AttributeFilter(value = ContainsIgnoreCaseFilter.class, name = "like")
    })
    @JsonProperty("city")
    @Schema(description = "Thành phố", example = "Hồ Chí Minh")
    String getCity();

    @JsonProperty("company")
    @Schema(description = "Tên công ty", example = "Công ty TNHH ABC")
    String getCompany();

    @AttributeFilters({
            @AttributeFilter(value = EqualFilter.class, name = "eq")
    })
    @JsonProperty("country")
    @Schema(description = "Quốc gia", example = "Việt Nam")
    String getCountry();

    @JsonProperty("first_name")
    @Schema(description = "Tên", example = "Văn A")
    String getFirstName();

    @JsonProperty("last_name")
    @Schema(description = "Họ", example = "Nguyễn")
    String getLastName();

    @JsonProperty("phone")
    @Schema(description = "Số điện thoại", example = "0912345678")
    String getPhone();

    @AttributeFilters({
            @AttributeFilter(value = EqualFilter.class, name = "eq")
    })
    @JsonProperty("province")
    @Schema(description = "Tỉnh/thành phố", example = "Hồ Chí Minh")
    String getProvince();

    @JsonProperty("zip")
    @Schema(description = "Mã bưu điện", example = "70000")
    String getZip();

    @JsonProperty("name")
    @Schema(description = "Tên đầy đủ", example = "Nguyễn Văn A")
    String getName();

    @AttributeFilters({
            @AttributeFilter(value = EqualFilter.class, name = "eq")
    })
    @JsonProperty("province_code")
    @Schema(description = "Mã tỉnh/thành phố", example = "79")
    String getProvinceCode();

    @AttributeFilters({
            @AttributeFilter(value = EqualFilter.class, name = "eq")
    })
    @JsonProperty("country_code")
    @Schema(description = "Mã quốc gia", example = "VN")
    String getCountryCode();

    @AttributeFilters({
            @AttributeFilter(value = EqualFilter.class, name = "eq")
    })
    @JsonProperty("default")
    @Schema(description = "Là địa chỉ mặc định", example = "true")
    Boolean getIsDefault();

    @AttributeFilters({
            @AttributeFilter(value = ContainsIgnoreCaseFilter.class, name = "like")
    })
    @JsonProperty("district")
    @Schema(description = "Quận/huyện", example = "Quận 1")
    String getDistrict();

    @AttributeFilters({
            @AttributeFilter(value = EqualFilter.class, name = "eq")
    })
    @JsonProperty("district_code")
    @Schema(description = "Mã quận/huyện", example = "760")
    String getDistrictCode();

    @AttributeFilters({
            @AttributeFilter(value = ContainsIgnoreCaseFilter.class, name = "like")
    })
    @JsonProperty("ward")
    @Schema(description = "Phường/xã", example = "Phường Bến Nghé")
    String getWard();

    @AttributeFilters({
            @AttributeFilter(value = EqualFilter.class, name = "eq")
    })
    @JsonProperty("ward_code")
    @Schema(description = "Mã phường/xã", example = "26734")
    String getWardCode();
} 