package hanniejewelry.vn.user.service;

import io.vavr.control.Try;
import lombok.RequiredArgsConstructor;
import hanniejewelry.vn.shared.exception.BaseMessageType;
import hanniejewelry.vn.shared.exception.BizException;
import hanniejewelry.vn.shared.utils.ExistenceChecker;
import hanniejewelry.vn.user.dto.UserInfoResponse;
import hanniejewelry.vn.user.dto.UserView;
import hanniejewelry.vn.user.entity.Role;
import hanniejewelry.vn.user.entity.User;
import hanniejewelry.vn.user.mapper.UserResponseMapper;
import hanniejewelry.vn.user.mapper.UserViewMapper;
import hanniejewelry.vn.user.repository.RoleRepository;
import hanniejewelry.vn.user.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepo;
    private final RoleRepository roleRepo;
    private final ExistenceChecker existenceChecker;
    private final UserViewMapper userViewMapper;
    private final UserResponseMapper userInfoResponseMapper;

    public List<UserView> getAll() {
        return Try.of(userRepo::findAll)
                .getOrElse(Collections::emptyList)
                .stream()
                .map(userViewMapper::toUserView)
                .toList();
    }

    public UserView updateUserInfo(UUID userId, String phone, Set<UUID> roleIds) {
        existenceChecker.validateAllExistOrThrow(Role.class, roleIds, "Không tìm thấy quyền với id: %s");
        User user = userRepo.findById(userId)
                .orElseThrow(() -> new BizException(BaseMessageType.NOT_FOUND, "User không tồn tại"));
        user.setPhone(phone);
        user.setRoles(new HashSet<>(roleRepo.findAllById(roleIds)));
        return userViewMapper.toUserView(userRepo.save(user));
    }

    public UserInfoResponse buildUserInfoResponse(User user) {
        return Try.of(() -> userInfoResponseMapper.toUserInfoResponse(user))
                .getOrElseThrow(() -> new BizException(BaseMessageType.INTERNAL_ERROR, "Lỗi hệ thống"));
    }

    public void deleteUser(UUID userId) {
        userRepo.findById(userId)
                .orElseThrow(() -> new BizException(BaseMessageType.NOT_FOUND, "User không tồn tại"));
        userRepo.softDeleteById(userId);
    }
}
