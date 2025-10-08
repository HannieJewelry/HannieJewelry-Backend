package hanniejewelry.vn.product.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InventoryRequest {
    @JsonProperty("locId")
    private Long locId;
    
    private String name;
    
    private Integer quantity;
    
    @JsonProperty("isPrimaryLocation")
    private boolean isPrimaryLocation;
} 