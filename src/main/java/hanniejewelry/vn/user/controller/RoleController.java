package hanniejewelry.vn.user.controller;

import hanniejewelry.vn.shared.dto.RestResponse;
import lombok.RequiredArgsConstructor;
import hanniejewelry.vn.shared.utils.RestResponseUtils;
import hanniejewelry.vn.user.dto.CreateRoleRequest;
import hanniejewelry.vn.user.dto.RoleView;
import hanniejewelry.vn.user.service.RoleService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;
import hanniejewelry.vn.shared.constants.ApiConstants;

@RestController
@RequestMapping(ApiConstants.API_PREFIX + "/group_permissions")
@RequiredArgsConstructor
public class RoleController {
    private final RoleService roleService;

    @PostMapping
    public ResponseEntity<RestResponse<RoleView>> create(@RequestBody CreateRoleRequest req) {
        return RestResponseUtils.createdResponse(roleService.create(req));
    }

    @GetMapping
    public ResponseEntity<RestResponse<List<RoleView>>> getAll() {
        return RestResponseUtils.successResponse(roleService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<RestResponse<RoleView>> get(@PathVariable UUID id) {
        return RestResponseUtils.successResponse(roleService.get(id));
    }
}
