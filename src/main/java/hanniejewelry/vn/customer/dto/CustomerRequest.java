package hanniejewelry.vn.customer.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import hanniejewelry.vn.customer.enums.Gender;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.*;

import java.time.Instant;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class CustomerRequest {

    @Builder.Default
    private Boolean isAcceptMarketing = false;

    @Email(message = "Email must be valid")
    private String email;

    @Pattern(regexp = "^[0-9+\\-\\s()]+$", message = "Phone number format is invalid")
    private String phone;

    @Size(max = 100, message = "First name cannot exceed 100 characters")
    private String firstName;

    @Size(max = 100, message = "Last name cannot exceed 100 characters")
    private String lastName;

    @Size(max = 1000, message = "Note cannot exceed 1000 characters")
    private String note;

    private String tags;

    private Instant birthday;

    private Gender gender;

    @Valid
    private List<CustomerAddressRequest> addresses;

    @Valid
    private EInvoiceInfoRequest eInvoiceInfo;
} 