package hanniejewelry.vn.user.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import java.time.Instant;
import java.util.Set;
import java.util.UUID;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record UserView(
        UUID id,
        String phone,
        String email,
        Boolean isOwner,
        Integer roleType,
        Instant lastLoginDate,
        Integer status,
        Boolean isDeleted,
        Set<String> roles,
        Set<String> permissions
) {}
