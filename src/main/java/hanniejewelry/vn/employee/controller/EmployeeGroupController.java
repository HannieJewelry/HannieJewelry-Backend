package hanniejewelry.vn.employee.controller;

import hanniejewelry.vn.employee.dto.EmployeeGroupRequest;
import hanniejewelry.vn.employee.service.EmployeeGroupService;
import hanniejewelry.vn.employee.view.EmployeeGroupView;
import hanniejewelry.vn.employee.view.EmployeeInGroupView;
import hanniejewelry.vn.shared.dto.GenericFilterRequest;
import hanniejewelry.vn.shared.dto.RestResponse;
import hanniejewelry.vn.shared.utils.RestResponseUtils;
import hanniejewelry.vn.shared.dto.PagedResponse;
import hanniejewelry.vn.shared.utils.ResponseUtils;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

import static hanniejewelry.vn.shared.constants.ApiConstants.API_PREFIX;


@RestController
@RequestMapping(API_PREFIX + "/employee_groups")
@RequiredArgsConstructor
public class EmployeeGroupController {

    private final EmployeeGroupService employeeGroupService;

    @GetMapping()
    public ResponseEntity<RestResponse<PagedResponse<EmployeeGroupView>>> getAllEmployeeGroups(
            @ModelAttribute GenericFilterRequest filter) {
        return RestResponseUtils.successResponse(
                ResponseUtils.pagedSuccess(employeeGroupService.getAllEmployeeGroupsWithFilter(filter), filter)
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<RestResponse<EmployeeGroupView>> getEmployeeGroupById(@PathVariable UUID id) {
        return RestResponseUtils.successResponse(
                employeeGroupService.getEmployeeGroupById(id)
        );
    }

    @PostMapping()
    public ResponseEntity<RestResponse<EmployeeGroupView>> createEmployeeGroup(@Valid @RequestBody EmployeeGroupRequest request) {
        EmployeeGroupView result = employeeGroupService.createEmployeeGroup(request);
        return RestResponseUtils.createdResponse(result);
    }

    @PutMapping("/{id}")
    public ResponseEntity<RestResponse<EmployeeGroupView>> updateEmployeeGroup(
            @PathVariable UUID id, @Valid @RequestBody EmployeeGroupRequest request) {
        return RestResponseUtils.successResponse(
                employeeGroupService.updateEmployeeGroup(id, request)
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<RestResponse<Void>> deleteEmployeeGroup(@PathVariable UUID id) {
        employeeGroupService.deleteEmployeeGroup(id);
        return RestResponseUtils.successResponse(null);
    }

    @GetMapping("/useringroup")
    public ResponseEntity<RestResponse<List<EmployeeInGroupView>>> getEmployeesInGroup(@RequestParam List<UUID> ids) {
        return RestResponseUtils.successResponse(employeeGroupService.getEmployeesInGroup(ids));

    }
}
