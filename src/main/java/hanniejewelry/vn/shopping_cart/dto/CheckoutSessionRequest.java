package hanniejewelry.vn.shopping_cart.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

import java.util.List;
import java.util.UUID;


@Data
public class CheckoutSessionRequest {
    
    @Valid
    @NotEmpty(message = "At least one attribute is required")
    private List<CartAttributeRequest> attributes;
    private UUID addressId;
    private String note;
    
    private String email;
    
    private String phoneNumber;
    
    private String fullName;
    
    private ShippingAddressRequest shippingAddress;
    
    private Long shippingMethodId;
    
    private Long paymentMethodId;
    
    @Data
    public static class ShippingAddressRequest {
        private String address;
        private String city;
        private String zipCode;
        private String company;
        private Long countryId;
        private Long provinceId;
        private Long districtId;
        private Long wardId;
    }
} 