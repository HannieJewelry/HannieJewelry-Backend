package hanniejewelry.vn.user.mapper;

import hanniejewelry.vn.shared.constants.AppConstants;
import hanniejewelry.vn.user.dto.UserInfoResponse;
import hanniejewelry.vn.user.entity.Role;
import hanniejewelry.vn.user.entity.User;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class UserResponseMapper {
    public UserInfoResponse toUserInfoResponse(User user) {
        Map<String, Object> userJson = new HashMap<>();
        userJson.put("id", user.getId());
        
        String displayName = (user.getEmployee() != null && user.getEmployee().getFullName() != null)
            ? user.getEmployee().getFullName() 
            : user.getEmail();
        userJson.put("name", displayName);
        
        userJson.put("email", user.getEmail());
        userJson.put("orgId", 1);
        userJson.put("orgName", "hanniejewelry");
        userJson.put("picture", null);
        userJson.put("roles", user.getRoles().stream().map(Role::getCode).toList());

        Set<String> allScopes = new HashSet<>();
        user.getRoles().forEach(role -> {
            if (role.getPermissions() != null) allScopes.addAll(role.getPermissions());
            allScopes.add(role.getCode());
        });

        boolean isAdmin = user.getRoles().stream().anyMatch(role -> "admin".equalsIgnoreCase(role.getCode()));

        return UserInfoResponse.builder()
                .user(userJson)
                .isProduction(true)
                .authority(AppConstants.FRONTEND_BASE_URL + "/auth/realms/hanniejewelry")
                .loginUrl(AppConstants.FRONTEND_BASE_URL + "/auth/login")
                .domainHost(AppConstants.FRONTEND_BASE_URL.replace("http://", "").replace("https://", "")) // loại bỏ http(s):// nếu muốn lấy domain
                .basePath("/admin")
                .isWebView(false)
                .isNewbie(false)
                .scopes(new ArrayList<>(allScopes))
                .isAdmin(isAdmin)
                .build();
    }
}
