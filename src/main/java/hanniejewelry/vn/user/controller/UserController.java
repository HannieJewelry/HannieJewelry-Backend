package hanniejewelry.vn.user.controller;

import hanniejewelry.vn.shared.dto.RestResponse;
import hanniejewelry.vn.shared.utils.RestResponseUtils;
import lombok.RequiredArgsConstructor;
import hanniejewelry.vn.user.dto.InviteUserRequest;
import hanniejewelry.vn.user.dto.UpdateUserRequest;
import hanniejewelry.vn.user.dto.UserInfoResponse;
import hanniejewelry.vn.user.dto.UserView;
import hanniejewelry.vn.user.entity.User;
import hanniejewelry.vn.user.service.InviteService;
import hanniejewelry.vn.user.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import hanniejewelry.vn.shared.constants.ApiConstants;

@RestController
@RequestMapping(ApiConstants.API_PREFIX + "/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    private final InviteService inviteService;

    @PostMapping("/invite")
    public ResponseEntity<RestResponse<UserView>> invite(@RequestBody InviteUserRequest req) {
        return RestResponseUtils.createdResponse(inviteService.inviteUser(req));
    }

    @GetMapping("/info")
    public ResponseEntity<UserInfoResponse> getUserInfo(@AuthenticationPrincipal User user) {
        return ResponseEntity.ok(userService.buildUserInfoResponse(user));
    }

    @PostMapping("/accept-invite")
    public ResponseEntity<RestResponse<String>> acceptInvite(
            @RequestParam UUID u, @RequestParam String t, @RequestBody Map<String, String> body) {
        inviteService.acceptInvite(u, t, body.get("newPassword"));
        return RestResponseUtils.successResponse("Đổi mật khẩu thành công, tài khoản đã được kích hoạt!");
    }

    @GetMapping
    public ResponseEntity<List<UserView>> getAll() {
        return ResponseEntity.ok(userService.getAll());
    }

    @PutMapping("/{userId}")
    public ResponseEntity<RestResponse<UserView>> updateUser(
            @PathVariable UUID userId, @RequestBody UpdateUserRequest req) {
        return RestResponseUtils.successResponse(
                userService.updateUserInfo(userId, req.phone(), req.roleIds())
        );
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<RestResponse<String>> deleteUser(@PathVariable UUID userId) {
        userService.deleteUser(userId);
        return RestResponseUtils.successResponse("Đã xóa mềm user!");
    }

}
