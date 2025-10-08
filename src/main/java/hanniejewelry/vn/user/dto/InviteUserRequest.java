package hanniejewelry.vn.user.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import hanniejewelry.vn.user.enums.UserType;
import jakarta.validation.constraints.*;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record InviteUserRequest(
        @NotBlank String name,
        @NotBlank @Email String username,
        @NotNull UserType type
) {}
