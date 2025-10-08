package hanniejewelry.vn.customer.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class EInvoiceInfoRequest {

    private String name;

    private String taxCode;

    private String email;

    private String address;

    @Builder.Default
    private Boolean isCompany = false;
} 