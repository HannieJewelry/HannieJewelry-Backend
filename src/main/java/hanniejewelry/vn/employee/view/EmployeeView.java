package hanniejewelry.vn.employee.view;

import com.blazebit.persistence.view.EntityView;
import com.blazebit.persistence.view.IdMapping;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import hanniejewelry.vn.employee.entity.Employee;
import hanniejewelry.vn.user.entity.User;
import hanniejewelry.vn.user.enums.UserStatus;
import hanniejewelry.vn.user.enums.UserType;

import java.time.Instant;
import java.util.UUID;

@EntityView(Employee.class)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public interface EmployeeView {

    @IdMapping
    UUID getId();

    String getFullName();

    Instant getCreatedAt();

    Instant getUpdatedAt();

    @EntityView(User.class)
    interface UserView {
        UUID getId();
        String getEmail();
        String getPhone();
      UserType getUserType();
      UserStatus getStatus();
    }

    UserView getUser();
} 