package hanniejewelry.vn.inventory.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class SupplierRequest {

    private String firstName;
    
    private String lastName;
    
    @NotBlank(message = "Supplier name is required")
    private String supplierName;
    
    private String phone;
    
    private String email;
    
    @NotNull(message = "Status is required")
    private Integer status;
    
    private String zipCode;
    
    private Integer countryId;
    
    private String location;
    
    private String address;
    
    private Integer provinceId;
    
    private Integer districtId;
    
    private String city;
    
    private Integer wardId;
    
    private String company;
    
    private String taxCode;
    
    private String notes;
    
    private String tags;
    
    private String contactName;
    
    private String postalZipCode;
} 