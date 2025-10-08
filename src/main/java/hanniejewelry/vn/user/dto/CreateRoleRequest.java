package hanniejewelry.vn.user.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import jakarta.validation.constraints.*;
import java.util.Set;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record CreateRoleRequest(
        @NotBlank String code,
        @NotBlank String name,
        String description,
        Boolean isSystem,
        @NotNull Set<String> permissions
) {}
