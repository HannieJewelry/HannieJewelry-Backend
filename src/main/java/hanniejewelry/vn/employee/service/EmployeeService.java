package hanniejewelry.vn.employee.service;

import com.blazebit.persistence.CriteriaBuilderFactory;
import com.blazebit.persistence.PagedList;
import com.blazebit.persistence.view.EntityViewManager;
import com.blazebit.persistence.view.EntityViewSetting;
import hanniejewelry.vn.employee.dto.InviteEmployeeRequest;
import hanniejewelry.vn.employee.dto.EmployeeRequest;
import hanniejewelry.vn.employee.entity.Employee;
import hanniejewelry.vn.employee.repository.EmployeeRepository;
import hanniejewelry.vn.employee.view.EmployeeView;
import hanniejewelry.vn.shared.exception.BaseMessageType;
import hanniejewelry.vn.shared.exception.BizException;
import hanniejewelry.vn.shared.filter.GenericBlazeFilterApplier;
import hanniejewelry.vn.shared.dto.GenericFilterRequest;
import hanniejewelry.vn.user.entity.User;
import hanniejewelry.vn.user.repository.UserRepository;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@Transactional
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class EmployeeService {

    EmployeeRepository employeeRepository;
    UserRepository userRepository;
    EntityViewManager evm;
    CriteriaBuilderFactory cbf;
    EntityManager em;

    private final GenericBlazeFilterApplier<EmployeeView> filterApplier = new GenericBlazeFilterApplier<>() {
    };

    public PagedList<EmployeeView> getAllEmployeesWithFilter(GenericFilterRequest filter) {
        var setting = EntityViewSetting.create(EmployeeView.class, filter.getPage(), filter.getSize());
        filterApplier.applyFilters(setting, filter);

        var cb = cbf.create(em, Employee.class);
        filterApplier.applySort(cb, filter);

        return evm.applySetting(setting, cb).getResultList();
    }

    public EmployeeView getEmployeeById(UUID id) {
        if (!employeeRepository.existsById(id)) {
            throw new BizException(BaseMessageType.NOT_FOUND, "Employee not found with id: {0}", id);
        }
        return evm.find(em, EmployeeView.class, id);
    }

    public EmployeeView getEmployeeByUserId(UUID userId) {
        Employee employee = employeeRepository.findByUserId(userId)
                .orElseThrow(() -> new BizException(BaseMessageType.NOT_FOUND, "Employee not found for user id: {0}", userId));
        return evm.find(em, EmployeeView.class, employee.getId());
    }

    public EmployeeView createEmployee(InviteEmployeeRequest request) {
        // Validate user exists and is an employee type
        User user = userRepository.findById(request.userId())
                .orElseThrow(() -> new BizException(BaseMessageType.NOT_FOUND, "User not found with id: {0}", request.userId()));

//        if (user.getUserType() != UserType.EMPLOYEE) {
//            throw new BizException(BaseMessageType.INVALID_INPUT, "User must be of type EMPLOYEE");
//        }

        // Check if employee already exists for this user
        if (employeeRepository.existsByUserId(request.userId())) {
            throw new BizException(BaseMessageType.ENTITY_ALREADY_EXISTS, "Employee already exists for user id: {0}", request.userId());
        }

        Employee saved = employeeRepository.save(Employee.builder()
                .fullName(request.fullName())
                .user(user)
                .build());

        employeeRepository.flush();
        return evm.find(em, EmployeeView.class, saved.getId());
    }

    public EmployeeView updateEmployee(UUID id, EmployeeRequest request) {
        Employee existing = employeeRepository.findById(id)
                .orElseThrow(() -> new BizException(BaseMessageType.NOT_FOUND, "Employee not found with id: {0}", id));

        existing.setFullName(request.fullName());

        Employee saved = employeeRepository.save(existing);
        employeeRepository.flush();
        return evm.find(em, EmployeeView.class, saved.getId());
    }

    public void deleteEmployee(UUID id) {
        if (!employeeRepository.existsById(id)) {
            throw new BizException(BaseMessageType.NOT_FOUND, "Employee not found with id: {0}", id);
        }
        employeeRepository.softDeleteById(id);
        employeeRepository.flush();
    }

    public long countEmployees() {
        return employeeRepository.count();
    }
} 