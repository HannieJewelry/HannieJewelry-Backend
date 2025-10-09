package hanniejewelry.vn.customer.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
        import lombok.*;

        import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@Schema(description = "Thông tin địa chỉ khách hàng")
public class CustomerAddressRequest {

    @NotBlank(message = "Address line 1 is required")
    @Size(max = 255, message = "Address line 1 cannot exceed 255 characters")
    @Schema(description = "Địa chỉ dòng 1", example = "123 Đường Nguyễn Huệ")
    private String address1;

    @Size(max = 255, message = "Address line 2 cannot exceed 255 characters")
    @Schema(description = "Địa chỉ dòng 2", example = "Phường Bến Nghé")
    private String address2;

    @Size(max = 100, message = "City cannot exceed 100 characters")
    @Schema(description = "Thành phố", example = "Hồ Chí Minh")
    private String city;

    @Size(max = 100, message = "First name cannot exceed 100 characters")
    @Schema(description = "Tên", example = "Văn A")
    private String firstName;

    @Size(max = 255, message = "Company cannot exceed 255 characters")
    @Schema(description = "Tên công ty", example = "Công ty TNHH ABC")
    private String company;

    @Pattern(regexp = "^[0-9+\\-\\s()]*$", message = "Phone number format is invalid")
    @Schema(description = "Số điện thoại", example = "0912345678")
    private String phone;

    @Size(max = 100, message = "Last name cannot exceed 100 characters")
    @Schema(description = "Họ", example = "Nguyễn")
    private String lastName;

    @Size(max = 20, message = "ZIP code cannot exceed 20 characters")
    @Schema(description = "Mã bưu điện", example = "70000")
    private String zip;

    @Size(max = 10, message = "Province code cannot exceed 10 characters")
    @Schema(description = "Mã tỉnh/thành phố", example = "79")
    private String provinceCode;

    @Size(max = 10, message = "Country code cannot exceed 10 characters")
    @Schema(description = "Mã quốc gia", example = "VN")
    private String countryCode;

    @Size(max = 20, message = "District code cannot exceed 20 characters")
    @Schema(description = "Mã quận/huyện", example = "760")
    private String districtCode;

    @Size(max = 20, message = "Ward code cannot exceed 20 characters")
    @Schema(description = "Mã phường/xã", example = "26734")
    private String wardCode;

    // These fields are optional and not part of the input request but may be used internally
    @Schema(hidden = true)
    private UUID id;

    @Schema(hidden = true)
    private String country;

    @Schema(hidden = true)
    private String province;

    @Schema(hidden = true)
    private String name;

    @Schema(hidden = true)
    private String countryName;

    @Schema(description = "Đặt làm địa chỉ mặc định", example = "true")
    private Boolean isDefault;

    @Schema(hidden = true)
    private String district;

    @Schema(hidden = true)
    private String ward;

    @Schema(hidden = true)
    private UUID customerId;
}