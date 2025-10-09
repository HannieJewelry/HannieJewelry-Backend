package hanniejewelry.vn.config;

import hanniejewelry.vn.user.entity.Role;
import hanniejewelry.vn.user.entity.User;
import hanniejewelry.vn.user.enums.UserType;
import hanniejewelry.vn.user.enums.UserStatus;
import hanniejewelry.vn.user.repository.RoleRepository;
import hanniejewelry.vn.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Set;
import java.util.UUID;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class AdminUserInitializer {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    @Bean
    public CommandLineRunner initAdminUser() {
        return args -> {
            // Check if admin user already exists
            if (userRepository.findByEmail("admin@gmail.com").isEmpty()) {
                log.info("Creating default admin user");

                // Find or create ADMIN role
                Role adminRole = roleRepository.findByCode("ADMIN")
                        .orElseGet(() -> {
                            Role role = new Role();
                            role.setId(UUID.randomUUID());
                            role.setCode("ADMIN");
                            role.setName("Administrator");
                            role.setDescription("System Administrator");
                            role.setSystem(true);
                            return roleRepository.save(role);
                        });

                // Create admin user
                User adminUser = User.builder()
                        .id(UUID.randomUUID())
                        .email("admin@gmail.com")
                        .password(passwordEncoder.encode("admin123"))
                        .phone("0901234567")
                        .isOwner(true)
                        .verified(true)
                        .userType(UserType.EMPLOYEE)
                        .status(UserStatus.ENABLED)
                        .roles(Set.of(adminRole))
                        .build();

                userRepository.save(adminUser);
                log.info("Default admin user created successfully with email: admin@gmail.com and password: admin123");
            } else {
                log.info("Admin user already exists, skipping initialization");
            }
        };
    }
}