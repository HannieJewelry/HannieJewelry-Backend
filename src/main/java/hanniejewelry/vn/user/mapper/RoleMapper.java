package hanniejewelry.vn.user.mapper;

import hanniejewelry.vn.user.entity.Role;
import hanniejewelry.vn.user.dto.RoleView;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface RoleMapper {

    RoleView toView(Role role);
}
