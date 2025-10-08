package hanniejewelry.vn.shipping.api.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import hanniejewelry.vn.shipping.domain.model.enums.CODStatus;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class UpdateCODStatusRequest {
    
    @NotNull(message = "Status is required")
    private CODStatus status;
    
    private BigDecimal amount;
    
    private String note;
} 