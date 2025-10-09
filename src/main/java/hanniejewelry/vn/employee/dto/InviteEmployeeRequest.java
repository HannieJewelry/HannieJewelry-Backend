package hanniejewelry.vn.employee.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.util.UUID;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record InviteEmployeeRequest(
        @NotBlank(message = "Full name is required")
        @Size(max = 200, message = "Full name cannot exceed 200 characters")
        String fullName,

        UUID userId
) {
} 