package hanniejewelry.vn.employee.view;

import com.blazebit.persistence.view.EntityView;
import com.blazebit.persistence.view.IdMapping;
import com.blazebit.persistence.view.Mapping;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import hanniejewelry.vn.employee.entity.EmployeeGroup;

import java.time.Instant;
import java.util.Set;
import java.util.UUID;

@EntityView(EmployeeGroup.class)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public interface EmployeeGroupView {
    @IdMapping
    UUID getId();
    String getGroupName();
    Integer getTypeId();

    @Mapping("employees.id")
    Set<UUID> getEmployeeIds();

    Instant getCreatedAt();
    Instant getUpdatedAt();
}