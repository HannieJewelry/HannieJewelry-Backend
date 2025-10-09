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
import java.util.List;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class ShipmentResponse {
    private UUID id;
    private UUID orderId;
    private String orderNumber;
    private String carrierCode;
    private String carrierName;
    private String trackingNumber;
    private String trackingUrl;
    private ShipmentStatus status;
    private String statusName;
    private BigDecimal codAmount;
    private CODStatus codStatus;
    private String codStatusName;
    private Integer printCount;
    private Instant lastPrintedDate;
    private String notes;
    private Instant createdAt;
    private Instant updatedAt;
    private List<ShipmentItemResponse> items;
    
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public static class ShipmentItemResponse {
        private UUID id;
        private UUID orderLineItemId;
        private UUID productId;
        private Long variantId;
        private Integer quantity;
    }
} 