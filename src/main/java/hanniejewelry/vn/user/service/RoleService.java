package hanniejewelry.vn.user.service;

import hanniejewelry.vn.user.dto.CreateRoleRequest;
import hanniejewelry.vn.user.dto.RoleView;
import hanniejewelry.vn.user.entity.Role;
import hanniejewelry.vn.user.mapper.RoleMapper;
import hanniejewelry.vn.user.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RoleService {
    private final RoleRepository roleRepo;
    private final RoleMapper roleMapper;

    public RoleView create(CreateRoleRequest req) {
        if (roleRepo.findByCode(req.code()).isPresent())
            throw new RuntimeException("Role code exists");
        Role role = Role.builder()
                .code(req.code())
                .name(req.name())
                .description(req.description())
                .isSystem(req.isSystem() != null ? req.isSystem() : false)
                .permissions(req.permissions())
                .build();
        role = roleRepo.save(role);
        return roleMapper.toView(role);
    }

    public List<RoleView> getAll() {
        return roleRepo.findAll().stream()
                .map(roleMapper::toView)
                .toList();
    }

    public RoleView get(UUID id) {
        Role role = roleRepo.findById(id).orElseThrow();
        return roleMapper.toView(role);
    }
}
