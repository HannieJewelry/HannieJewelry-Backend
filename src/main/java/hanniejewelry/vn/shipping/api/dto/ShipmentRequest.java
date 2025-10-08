package hanniejewelry.vn.shipping.api.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class ShipmentRequest {
    
    @NotNull(message = "Order ID is required")
    private UUID orderId;
    
    private String carrierCode;
    
    private String carrierName;
    
    private String trackingNumber;
    
    private String trackingUrl;
    
    private String notes;
} 