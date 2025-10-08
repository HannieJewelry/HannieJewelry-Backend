package hanniejewelry.vn.employee.service;

import com.blazebit.persistence.CriteriaBuilderFactory;
import com.blazebit.persistence.PagedList;
import com.blazebit.persistence.view.EntityViewManager;
import com.blazebit.persistence.view.EntityViewSetting;
import hanniejewelry.vn.employee.dto.EmployeeGroupRequest;
import hanniejewelry.vn.employee.entity.Employee;
import hanniejewelry.vn.employee.entity.EmployeeGroup;
import hanniejewelry.vn.employee.repository.EmployeeRepository;
import hanniejewelry.vn.employee.repository.EmployeeGroupRepository;
import hanniejewelry.vn.employee.view.EmployeeGroupView;
import hanniejewelry.vn.employee.view.EmployeeInGroupView;
import hanniejewelry.vn.shared.exception.BaseMessageType;
import hanniejewelry.vn.shared.exception.BizException;
import hanniejewelry.vn.shared.filter.GenericBlazeFilterApplier;
import hanniejewelry.vn.shared.dto.GenericFilterRequest;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class EmployeeGroupService {

    EmployeeGroupRepository employeeGroupRepository;
    EmployeeRepository employeeRepository;
    EntityViewManager evm;
    CriteriaBuilderFactory cbf;
    EntityManager em;

    private final GenericBlazeFilterApplier<EmployeeGroupView> filterApplier = new GenericBlazeFilterApplier<>() {};

    public PagedList<EmployeeGroupView> getAllEmployeeGroupsWithFilter(GenericFilterRequest filter) {
        var setting = EntityViewSetting.create(EmployeeGroupView.class, filter.getPage(), filter.getSize());
        filterApplier.applyFilters(setting, filter);
        var cb = cbf.create(em, EmployeeGroup.class);
        filterApplier.applySort(cb, filter);
        return evm.applySetting(setting, cb).getResultList();
    }

    public EmployeeGroupView getEmployeeGroupById(UUID id) {
        if (!employeeGroupRepository.existsById(id)) {
            throw new BizException(BaseMessageType.NOT_FOUND, "Employee group not found with id: {0}", id);
        }
        return evm.find(em, EmployeeGroupView.class, id);
    }

    public EmployeeGroupView createEmployeeGroup(EmployeeGroupRequest request) {
        Set<Employee> employees = new HashSet<>();
        if (request.getEmployeeIds() != null && !request.getEmployeeIds().isEmpty()) {
            employees = request.getEmployeeIds().stream()
                    .map(employeeId -> employeeRepository.findById(employeeId)
                            .orElseThrow(() -> new BizException(BaseMessageType.NOT_FOUND, "Employee not found with id: {0}", employeeId)))
                    .collect(Collectors.toSet());
        }

        EmployeeGroup employeeGroup = EmployeeGroup.builder()
                .groupName(request.getGroupName())
                .typeId(request.getTypeId())
                .employees(employees)
                .build();

        EmployeeGroup saved = employeeGroupRepository.save(employeeGroup);
        employeeGroupRepository.flush();
        return evm.find(em, EmployeeGroupView.class, saved.getId());
    }

    public EmployeeGroupView updateEmployeeGroup(UUID id, EmployeeGroupRequest request) {
        EmployeeGroup existingGroup = employeeGroupRepository.findById(id)
                .orElseThrow(() -> new BizException(BaseMessageType.NOT_FOUND, "Employee group not found with id: {0}", id));

        Set<Employee> employees = new HashSet<>();
        if (request.getEmployeeIds() != null && !request.getEmployeeIds().isEmpty()) {
            employees = request.getEmployeeIds().stream()
                    .map(employeeId -> employeeRepository.findById(employeeId)
                            .orElseThrow(() -> new BizException(BaseMessageType.NOT_FOUND, "Employee not found with id: {0}", employeeId)))
                    .collect(Collectors.toSet());
        }

        existingGroup.setGroupName(request.getGroupName());
        existingGroup.setTypeId(request.getTypeId());
        existingGroup.setEmployees(employees);

        EmployeeGroup saved = employeeGroupRepository.save(existingGroup);
        employeeGroupRepository.flush();
        return evm.find(em, EmployeeGroupView.class, saved.getId());
    }

    public List<EmployeeInGroupView> getEmployeesInGroup(List<UUID> ids) {
        var setting = EntityViewSetting.create(EmployeeInGroupView.class);
        var cb = cbf.create(em, Employee.class);
        cb.where("id").in(ids);
        return evm.applySetting(setting, cb).getResultList();
    }

    public void deleteEmployeeGroup(UUID id) {
        if (!employeeGroupRepository.existsById(id)) {
            throw new BizException(BaseMessageType.NOT_FOUND, "Employee group not found with id: {0}", id);
        }
        employeeGroupRepository.softDeleteById(id);
        employeeGroupRepository.flush();
    }
}
