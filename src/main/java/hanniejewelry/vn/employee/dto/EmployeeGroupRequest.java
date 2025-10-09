package hanniejewelry.vn.employee.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Set;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class EmployeeGroupRequest {
    @NotBlank(message = "Group name is required")
    @Size(max = 255, message = "Group name cannot exceed 255 characters")
    private String groupName;
    
    @NotNull(message = "Type ID is required")
    private Integer typeId;

    private Set<UUID> employeeIds;
} 