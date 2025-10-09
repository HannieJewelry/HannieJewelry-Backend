package hanniejewelry.vn.shipping.api.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import hanniejewelry.vn.shipping.domain.model.enums.CODStatus;
import hanniejewelry.vn.shipping.domain.model.enums.ShipmentStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class ShipmentStatusResponse {
    private UUID id;
    private UUID orderId;
    private String orderNumber;
    private Instant updatedAt;
    
    // Status fields
    private ShipmentStatus shipmentStatus;
    private String shipmentStatusName;
    private CODStatus codStatus;
    private String codStatusName;
    private BigDecimal codAmount;
} 