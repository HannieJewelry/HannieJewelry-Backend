package hanniejewelry.vn.user.repository;


import hanniejewelry.vn.shared.infrastructure.impl.BaseRepository;
import hanniejewelry.vn.user.entity.User;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends BaseRepository<User, UUID> {
    Optional<User> findByEmail(String email);
    Optional<User> findByPhone(String phone);
}
