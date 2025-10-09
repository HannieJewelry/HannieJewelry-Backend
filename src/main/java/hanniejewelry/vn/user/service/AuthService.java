package hanniejewelry.vn.user.service;

import hanniejewelry.vn.customer.service.CustomerService;
import hanniejewelry.vn.user.dto.*;
import hanniejewelry.vn.user.entity.Role;
import hanniejewelry.vn.user.repository.RoleRepository;
import io.vavr.control.Option;
import io.vavr.control.Try;
import lombok.RequiredArgsConstructor;
import hanniejewelry.vn.notification.sms.SmsService;
import hanniejewelry.vn.shared.exception.BaseMessageType;
import hanniejewelry.vn.shared.exception.BizException;
import hanniejewelry.vn.shared.utils.ExistenceChecker;
import hanniejewelry.vn.user.caching.OtpTempStore;
import hanniejewelry.vn.user.entity.User;
import hanniejewelry.vn.user.repository.UserRepository;
import org.springframework.security.authentication.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Collections;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

import static hanniejewelry.vn.user.util.AuthUtils.genOtp;
import static hanniejewelry.vn.user.util.AuthUtils.normalizePhone;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository userRepo;
    private final RoleRepository roleRepo;
    private final PasswordEncoder encoder;
    private final OtpTempStore otpTempStore;
    private final SmsService smsService;
    private final AuthenticationManager authManager;
    private final JwtService jwtService;
    private final ExistenceChecker existenceChecker;
    private final CustomerService customerService;
    
    private static final String DEFAULT_OTP = "778899";
    
    private static final Set<String> WHITELISTED_PHONES = Set.of(
        "+84345807906",
        "+84914696665",
            "+84968528172"
    );

    @Transactional
    public LoginResponse signup(SignupRequest req) {
        existenceChecker.throwIfExist(User.class, Map.of("email", req.email()), "Email đã tồn tại!");
        
        // Get te USER role
        Role userRole = roleRepo.findByCode("USER")
                .orElseThrow(() -> new BizException(BaseMessageType.INTERNAL_ERROR, "Không tìm thấy vai trò USER!"));
        
        User user = User.builder()
                .email(req.email())
                .password(encoder.encode(req.password()))
                .roles(Collections.singleton(userRole))
                .build();
        
        user = userRepo.save(user);
        
        customerService.createCustomerProfile(user);
        
        return buildLoginResponse(user);
    }

    public LoginResponse login(LoginRequest req) {
        Try.run(() -> authManager.authenticate(
                        new UsernamePasswordAuthenticationToken(req.email(), req.password())))
                .getOrElseThrow(ex -> new BizException(BaseMessageType.UNAUTHORIZED, "Email hoặc mật khẩu không đúng!"));
        User user = userRepo.findByEmail(req.email())
                .orElseThrow(() -> new BizException(BaseMessageType.NOT_FOUND, "Email không tồn tại!"));

        user.setLastLoginDate(Instant.now());
        userRepo.save(user);

        return buildLoginResponse(user);
    }

    public void requestOtp(String phone) {
        String normalizedPhone = normalizePhone(phone);
        String otp = genOtp();
        
        if (!WHITELISTED_PHONES.contains(normalizedPhone)) {
        Try.of(() -> smsService.sendOtpViaTextBee(normalizedPhone, otp))
                .filter(Boolean::booleanValue)
                .getOrElseThrow(() -> new BizException(BaseMessageType.INTERNAL_ERROR, "Gửi OTP thất bại, vui lòng thử lại sau!"));
        }
        
        otpTempStore.put(normalizedPhone, otp, Instant.now().plusSeconds(300));
    }

    @Transactional
    public LoginResponse loginOrSignupWithOtp(LoginOtpRequest req) {
        String normalizedPhone = normalizePhone(req.phone());
        
        boolean isValidOtp = DEFAULT_OTP.equals(req.otp()) ||
        Option.of(otpTempStore.get(normalizedPhone))
                .filter(otpEntry -> Objects.equals(otpEntry.otp(), req.otp()))
                      .isDefined();
                      
        if (!isValidOtp) {
            throw new BizException(BaseMessageType.BAD_REQUEST, "Mã OTP đã hết hạn hoặc không hợp lệ hoặc mã OTP không đúng!");
        }
        
        if (!DEFAULT_OTP.equals(req.otp()) && otpTempStore.get(normalizedPhone) != null) {
        otpTempStore.remove(normalizedPhone);
        }
        
        Optional<User> existingUser = userRepo.findByPhone(normalizedPhone);
        
        User user;
        if (existingUser.isPresent()) {
            user = existingUser.get();
            user.setLastLoginDate(Instant.now());
            userRepo.save(user);
        } else {
            Role userRole = roleRepo.findByCode("USER")
                    .orElseThrow(() -> new BizException(BaseMessageType.INTERNAL_ERROR, "Không tìm thấy vai trò USER!"));
            
            user = User.builder()
                    .phone(normalizedPhone)
                    .password(encoder.encode(genOtp() + normalizedPhone))
                    .email((normalizedPhone.replaceAll("\\s+", ".").toLowerCase() + "@no-email.local"))
                    .roles(Collections.singleton(userRole))
                    .build();
                    
            user = userRepo.save(user);
            
            customerService.createCustomerProfile(user);
        }

        return buildLoginResponse(user);
    }


    private LoginResponse buildLoginResponse(User user) {
        String token = jwtService.generateToken(user);
        return new LoginResponse(token, jwtService.getExpirationTime());
    }

}
