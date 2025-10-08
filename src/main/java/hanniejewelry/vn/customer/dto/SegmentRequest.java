package hanniejewelry.vn.customer.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import hanniejewelry.vn.customer.entity.CustomerSegment;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class SegmentRequest {

    @NotBlank(message = "Segment name is required")
    @Size(max = 255, message = "Segment name cannot exceed 255 characters")
    private String name;

    @Size(max = 1000, message = "Description cannot exceed 1000 characters")
    private String description;

    @NotNull(message = "Segment type is required")
    private CustomerSegment.SegmentType type;

    @Size(max = 5000, message = "Definition cannot exceed 5000 characters")
    private String definition;
} 