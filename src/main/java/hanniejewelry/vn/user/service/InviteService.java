package hanniejewelry.vn.user.service;

import hanniejewelry.vn.customer.entity.Customer;
import hanniejewelry.vn.customer.repository.CustomerRepository;
import hanniejewelry.vn.employee.dto.InviteEmployeeRequest;
import hanniejewelry.vn.employee.service.EmployeeService;
import hanniejewelry.vn.notification.email.EmailService;
import hanniejewelry.vn.shared.constants.AppConstants;
import hanniejewelry.vn.shared.exception.BaseMessageType;
import hanniejewelry.vn.shared.exception.BizException;
import hanniejewelry.vn.shared.utils.ExistenceChecker;
import hanniejewelry.vn.user.caching.CustomerInviteStore;
import hanniejewelry.vn.user.caching.InviteTokenStore;
import hanniejewelry.vn.user.dto.InviteUserRequest;
import hanniejewelry.vn.user.dto.UserView;
import hanniejewelry.vn.user.entity.User;
import hanniejewelry.vn.user.enums.UserType;
import hanniejewelry.vn.user.mapper.UserViewMapper;
import hanniejewelry.vn.user.repository.RoleRepository;
import hanniejewelry.vn.user.repository.UserRepository;
import io.vavr.control.Option;
import io.vavr.control.Try;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class InviteService {
    private final UserRepository userRepo;
    private final RoleRepository roleRepo;
    private final CustomerRepository customerRepo;
    private final EmailService emailService;
    private final PasswordEncoder encoder;
    private final InviteTokenStore inviteTokenStore;
    private final CustomerInviteStore customerInviteStore;
    private final UserViewMapper userViewMapper;
    private final ExistenceChecker existenceChecker;
    private final EmployeeService employeeService;

    public UserView inviteUser(InviteUserRequest req) {
        return Option.of(req.type())
                .filter(type -> type == UserType.USER)
                .map(__ -> Try.run(() -> existenceChecker.throwIfNotExist(Customer.class, 
                        Map.of("email", req.username()), "Email khách hàng không tồn tại!"))
                        .andThen(() -> existenceChecker.throwIfExist(User.class, 
                        Map.of("email", req.username()), "Email đã tồn tại!"))
                        .map(___ -> req))
                .getOrElse(() -> Try.run(() -> existenceChecker.throwIfExist(User.class, 
                        Map.of("email", req.username()), "Email đã tồn tại!"))
                        .map(__ -> req))
                .flatMap(validatedReq -> Option.ofOptional(roleRepo.findByCode(validatedReq.type().name()))
                        .toTry(() -> new BizException(BaseMessageType.NOT_FOUND, "Role " + validatedReq.type().name() + " không tồn tại"))
                        .flatMap(role -> Try.of(() -> userRepo.save(User.builder()
                                .email(validatedReq.username())
                                .userType(validatedReq.type())
                                .roles(Set.of(role))
                                .password(encoder.encode(UUID.randomUUID().toString()))
                                .build()))))
                .flatMap(user -> {
                    String inviteToken = UUID.randomUUID().toString();
                    return Try.run(() -> inviteTokenStore.put(user.getId(), inviteToken, Instant.now().plusSeconds(86400)))
                            .map(__ -> user);
                })
                .flatMap(user -> Option.of(req.type())
                        .filter(type -> type == UserType.EMPLOYEE)
                        .map(__ -> Try.run(() -> {
                            InviteEmployeeRequest inviteEmployeeRequest = new InviteEmployeeRequest(req.name(), user.getId());
                            employeeService.createEmployee(inviteEmployeeRequest);
                        }).map(___ -> user))
                        .orElse(Option.of(req.type())
                                .filter(type -> type == UserType.USER)
                                .flatMap(__ -> Option.ofOptional(customerRepo.findByEmail(req.username()))
                                        .map(customer -> Try.run(() -> customerInviteStore.storeCustomerRelationship(
                                                user.getId(), customer.getId(), Instant.now().plusSeconds(86400)))
                                                .map(___ -> user))))
                        .getOrElse(Try.success(user)))
                .flatMap(user -> Try.run(() -> {
                    String inviteToken = inviteTokenStore.get(user.getId());
                    String inviteUrl = String.format("%s/accept-invite?u=%s&t=%s&r=%s",
                            AppConstants.FRONTEND_BASE_URL,
                            user.getId(),
                            inviteToken,
                            URLEncoder.encode(AppConstants.FRONTEND_BASE_URL, StandardCharsets.UTF_8));
                    emailService.sendInvite(user.getEmail(), req.name(), inviteUrl, req.type());
                }).map(__ -> user))
                .map(userViewMapper::toUserView)
                .getOrElseThrow(ex -> new BizException(BaseMessageType.INTERNAL_ERROR, "Lỗi hệ thống: " + ex.getMessage()));
    }

    public void acceptInvite(UUID userId, String token, String newPassword) {
        Option.ofOptional(userRepo.findById(userId))
                .toTry(() -> new BizException(BaseMessageType.NOT_FOUND, "User không tồn tại"))
                .filterTry(user -> token.equals(inviteTokenStore.get(userId)),
                        () -> new BizException(BaseMessageType.BAD_REQUEST, "Token không hợp lệ hoặc đã hết hạn"))
                .flatMap(user -> Try.of(() -> {
                    user.setPassword(encoder.encode(newPassword));
                    user.setVerified(true);
                    return userRepo.save(user);
                }))
                .flatMap(user -> Option.of(user.getUserType())
                        .filter(type -> type == UserType.USER)
                        .flatMap(__ -> Option.of(customerInviteStore.getCustomerId(user.getId()))
                                .filter(customerId -> customerId != null)
                                .flatMap(customerId -> Option.ofOptional(customerRepo.findById(customerId))))
                        .map(customer -> Try.run(() -> {
                            customer.setUser(user);
                            customerRepo.save(customer);
                        }).map(__ -> user))
                        .getOrElse(Try.success(user)))
                .andThen(user -> {
                    inviteTokenStore.remove(userId);
                    customerInviteStore.removeCustomerRelationship(userId);
                })
                .getOrElseThrow(() -> new BizException(BaseMessageType.INTERNAL_ERROR, "Lỗi hệ thống"));
    }
}
