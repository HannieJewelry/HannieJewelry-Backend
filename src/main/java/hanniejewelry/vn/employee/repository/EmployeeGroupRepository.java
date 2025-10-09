package hanniejewelry.vn.employee.repository;

import hanniejewelry.vn.employee.entity.Employee;
import hanniejewelry.vn.employee.entity.EmployeeGroup;
import hanniejewelry.vn.shared.infrastructure.impl.BaseRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface EmployeeGroupRepository extends BaseRepository<EmployeeGroup, UUID> {
    
    List<EmployeeGroup> findByEmployeesContaining(Employee employee);
    
    List<EmployeeGroup> findByTypeId(Integer typeId);
    
    Optional<EmployeeGroup> findByIdAndTypeId(UUID id, Integer typeId);
} 