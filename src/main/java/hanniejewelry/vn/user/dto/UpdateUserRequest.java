package hanniejewelry.vn.user.dto;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.Set;
import java.util.UUID;

public record UpdateUserRequest(
        @NotBlank String phone,
        @NotNull Set<UUID> roleIds
) {}
