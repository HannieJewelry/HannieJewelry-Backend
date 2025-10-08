package hanniejewelry.vn.order.api.dto;


import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class OrderDTO {
    private UUID id;
    private String orderNumber;
    private BigDecimal totalPrice;
    private String financialStatus;
    private String fulfillmentStatus;
    private String name;
    private String email;
    private String gateway;
    private Instant createdAt;
    private String orderCode;
    private String orderProcessingStatus;
    private String tags;
    private String customerName;
    private Instant updatedAt;
}