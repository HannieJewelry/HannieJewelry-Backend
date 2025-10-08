package hanniejewelry.vn.product.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.*;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class ProductOptionRequest {
    private String name;
}
