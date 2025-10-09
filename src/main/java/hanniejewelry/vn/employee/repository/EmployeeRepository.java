package hanniejewelry.vn.employee.repository;

import hanniejewelry.vn.employee.entity.Employee;
import hanniejewelry.vn.shared.infrastructure.impl.BaseRepository;

import java.util.Optional;
import java.util.UUID;

public interface EmployeeRepository extends BaseRepository<Employee, UUID> {
    
    Optional<Employee> findByUserId(UUID userId);
    
    boolean existsByUserId(UUID userId);
} 