package hanniejewelry.vn.user.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import java.util.Set;
import java.util.UUID;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record RoleView(
        UUID id,
        String code,
        String name,
        String description,
        Boolean isSystem,
        Set<String> permissions
) {}
