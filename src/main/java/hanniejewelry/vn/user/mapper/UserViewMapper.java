package hanniejewelry.vn.user.mapper;

import hanniejewelry.vn.user.dto.UserView;
import hanniejewelry.vn.user.entity.Role;
import hanniejewelry.vn.user.entity.User;
import org.mapstruct.*;

import java.util.Set;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface UserViewMapper {

    @Mapping(target = "roles", expression = "java(mapRoles(user.getRoles()))")
    @Mapping(target = "permissions", expression = "java(mapPermissions(user.getRoles()))")
    UserView toUserView(User user);

    default Set<String> mapRoles(Set<Role> roles) {
        return roles == null ? Set.of() :
                roles.stream().map(Role::getCode).collect(Collectors.toSet());
    }

    default Set<String> mapPermissions(Set<Role> roles) {
        return roles == null ? Set.of() :
                roles.stream()
                        .flatMap(r -> r.getPermissions() == null ? null : r.getPermissions().stream())
                        .filter(java.util.Objects::nonNull)
                        .collect(Collectors.toSet());
    }
}
