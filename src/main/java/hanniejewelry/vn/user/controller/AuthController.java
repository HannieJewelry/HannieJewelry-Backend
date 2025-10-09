package hanniejewelry.vn.user.controller;

import hanniejewelry.vn.shared.utils.RestResponseUtils;
import hanniejewelry.vn.user.dto.*;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import hanniejewelry.vn.user.service.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import hanniejewelry.vn.shared.constants.ApiConstants;

@RestController
@RequestMapping(ApiConstants.API_PREFIX + "/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @PostMapping("/signup")
    public ResponseEntity<?> signup(@RequestBody SignupRequest req) {
        return RestResponseUtils.successResponse(authService.signup(req), "Đăng ký thành công");
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest req) {
        return RestResponseUtils.successResponse(authService.login(req), "Đăng nhập thành công");
    }

    @PostMapping("/request-otp")
    public ResponseEntity<?> requestOtp(@RequestBody RequestOtpLogin req) {
        authService.requestOtp(req.phone());
        return RestResponseUtils.successResponse(null, "Đã gửi mã OTP.");
    }

//    @PostMapping("/login-otp")
//    public ResponseEntity<?> loginOrSignupWithOtp(@RequestBody LoginOtpRequest req) {
//        return RestResponseUtils.successResponse(authService.loginOrSignupWithOtp(req), "Xác thực thành công");
//    }
@PostMapping("/login-otp")
public ResponseEntity<?> loginOrSignupWithOtp(@RequestBody LoginOtpRequest req, HttpServletResponse response) {
    LoginResponse loginResponse = authService.loginOrSignupWithOtp(req);

    // Set cookie HttpOnly
    Cookie cookie = new Cookie("auth-token", loginResponse.token());
    cookie.setHttpOnly(true);
    cookie.setSecure(true); // Để true nếu dùng HTTPS
    cookie.setPath("/");
    cookie.setMaxAge((int) (loginResponse.expiresIn() / 1000));

    response.addCookie(cookie);

    return RestResponseUtils.successResponse(loginResponse, "Xác thực thành công");
}

}
