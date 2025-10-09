package hanniejewelry.vn.employee.controller;

import hanniejewelry.vn.employee.dto.EmployeeRequest;
import hanniejewelry.vn.employee.service.EmployeeService;
import hanniejewelry.vn.employee.view.EmployeeView;
import hanniejewelry.vn.shared.dto.GenericFilterRequest;
import hanniejewelry.vn.shared.dto.RestResponse;
import hanniejewelry.vn.shared.utils.RestResponseUtils;
import hanniejewelry.vn.shared.dto.PagedResponse;
import hanniejewelry.vn.shared.utils.ResponseUtils;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.UUID;

import static hanniejewelry.vn.shared.constants.ApiConstants.API_PREFIX;

@RestController
@RequestMapping(API_PREFIX + "/employees")
@RequiredArgsConstructor
public class EmployeeController {

    private final EmployeeService employeeService;

    @GetMapping
    public ResponseEntity<RestResponse<PagedResponse<EmployeeView>>> getEmployees(
            @ModelAttribute GenericFilterRequest filter) {
        return RestResponseUtils.successResponse(
                ResponseUtils.pagedSuccess(employeeService.getAllEmployeesWithFilter(filter), filter)
        );
    }

    @GetMapping("/count")
    public ResponseEntity<RestResponse<Map<String, Long>>> countEmployees() {
        return RestResponseUtils.successResponse(
                Map.of("count", employeeService.countEmployees())
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<RestResponse<Map<String, EmployeeView>>> getEmployeeById(@PathVariable UUID id) {
        return RestResponseUtils.successResponse(
                Map.of("employee", employeeService.getEmployeeById(id))
        );
    }

    @GetMapping("/by-user/{userId}")
    public ResponseEntity<RestResponse<Map<String, EmployeeView>>> getEmployeeByUserId(@PathVariable UUID userId) {
        return RestResponseUtils.successResponse(
                Map.of("employee", employeeService.getEmployeeByUserId(userId))
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<RestResponse<Map<String, EmployeeView>>> updateEmployee(
            @PathVariable UUID id, @Valid @RequestBody EmployeeRequest request) {
        return RestResponseUtils.successResponse(
                Map.of("employee", employeeService.updateEmployee(id, request))
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<RestResponse<Void>> deleteEmployee(@PathVariable UUID id) {
        employeeService.deleteEmployee(id);
        return RestResponseUtils.successResponse(null);
    }
}