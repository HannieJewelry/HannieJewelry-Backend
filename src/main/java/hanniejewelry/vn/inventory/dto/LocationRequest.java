package hanniejewelry.vn.inventory.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class LocationRequest {

    @NotBlank(message = "Name is required")
    private String name;

    @NotBlank(message = "Location name is required")
    private String locationName;

    private String address;

    private String email;

    private String addressCont;

    private String phone;

    private String street;

    private String city;

    private Integer countryId;

    private Integer provinceId;

    private String postalZipCode;

    @NotNull(message = "Is shipping location flag is required")
    private Boolean isShippingLocation;

    private Boolean isUnavailableQty;

    private Double latitude;

    private Double longitude;

    private Integer districtId;

    private Integer wardId;

    private Boolean holdStock;

    private List<Long> userIds;

    private List<Long> groupIds;

    @NotNull(message = "Type ID is required")
    private Integer typeId;

    private Integer areaId;

    private Integer status;
} 