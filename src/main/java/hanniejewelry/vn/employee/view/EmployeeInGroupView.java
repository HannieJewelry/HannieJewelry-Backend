package hanniejewelry.vn.employee.view;

import com.blazebit.persistence.view.EntityView;
import com.blazebit.persistence.view.IdMapping;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import hanniejewelry.vn.employee.entity.Employee;

import java.util.UUID;

@EntityView(Employee.class)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public interface EmployeeInGroupView {
    
    @IdMapping
    UUID getId();
    
    String getFullName();

} 