package hanniejewelry.vn.shopping_cart.controller;

import hanniejewelry.vn.shopping_cart.entity.ShoppingCart;
import hanniejewelry.vn.shopping_cart.repository.ShoppingCartRepository;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
public class CartTokenUtils {

    private final ShoppingCartRepository shoppingCartRepository;

    private static final String CART_TOKEN_COOKIE = "cart_token";
    private static final int COOKIE_MAX_AGE = 30 * 24 * 60 * 60; // 30 ngày

    /**
     * Lấy cart_token: ưu tiên header, sau đó cookie. Không tự tạo mới ở đây!
     * @return token nếu có, null nếu không có
     */
    public String getCartToken(HttpServletRequest request) {
        String headerToken = request.getHeader("cart_token");
        if (headerToken != null && !headerToken.trim().isEmpty()) {
            log.debug("[CartTokenUtils] cart_token từ header: {}", headerToken);
            return headerToken;
        }
        if (request.getCookies() != null) {
            for (Cookie cookie : request.getCookies()) {
                if (CART_TOKEN_COOKIE.equals(cookie.getName())) {
                    log.debug("[CartTokenUtils] cart_token từ cookie: {}", cookie.getValue());
                    return cookie.getValue();
                }
            }
        }
        return null;
    }

    /**
     * Lấy cart_token đã tồn tại, hoặc tạo mới nếu không có/hết hạn.
     * Ưu tiên header hợp lệ, tiếp đến cookie hợp lệ, không thì tạo mới.
     */
//    public synchronized String getOrCreateCartToken(HttpServletRequest request, HttpServletResponse response) {
//        // 1. Ưu tiên cart_token từ header nếu hợp lệ
//        String headerToken = request.getHeader("cart_token");
//        if (headerToken != null && !headerToken.trim().isEmpty()) {
//            if (shoppingCartRepository.existsByToken(headerToken)) {
//                log.debug("[CartTokenUtils] Dùng cart_token hợp lệ từ header: {}", headerToken);
//                return headerToken;
//            } else {
//                log.warn("[CartTokenUtils] cart_token từ header không tồn tại trong DB: {}", headerToken);
//            }
//        }
//
//        // 2. Nếu chưa có, thử lấy cart_token từ cookie nếu hợp lệ
//        if (request.getCookies() != null) {
//            for (Cookie cookie : request.getCookies()) {
//                if (CART_TOKEN_COOKIE.equals(cookie.getName())) {
//                    String cookieToken = cookie.getValue();
//                    if (cookieToken != null && !cookieToken.trim().isEmpty()) {
//                        if (shoppingCartRepository.existsByToken(cookieToken)) {
//                            log.debug("[CartTokenUtils] Dùng cart_token hợp lệ từ cookie: {}", cookieToken);
//                            return cookieToken;
//                        } else {
//                            log.warn("[CartTokenUtils] cart_token từ cookie không tồn tại trong DB: {}", cookieToken);
//                        }
//                    }
//                }
//            }
//        }
//
//        // 3. XÁC ĐỊNH request có cho phép tạo mới cart hay không?
//        String method = request.getMethod();
//        String uri = request.getRequestURI();
//        boolean isCreateAction =
//                ("POST".equalsIgnoreCase(method) || "PUT".equalsIgnoreCase(method))
//                        && (uri.endsWith("/add") || uri.endsWith("/change") || uri.endsWith("/attributes") || uri.endsWith("/note"));
//
//        if (!isCreateAction) {
//            log.warn("[CartTokenUtils] Không tạo mới cart_token vì không phải thao tác tạo/sửa giỏ hàng! Method: {}, URI: {}", method, uri);
//            // Trả về null hoặc ném exception tùy business
//            return null;
//            // Hoặc: throw new BizException(BaseMessageType.NOT_FOUND, "Chưa có cart_token và không phải thao tác tạo giỏ hàng!");
//        }
//
//        // 4. Tạo mới token và cart nếu đến đây
//        String token = UUID.randomUUID().toString().replace("-", "");
//        ShoppingCart cart = new ShoppingCart();
//        cart.setToken(token);
//        cart.setItemCount(0);
//        cart.setTotalPrice(BigDecimal.ZERO);
//        cart.setTotalWeight(BigDecimal.ZERO);
//        cart.setRequiresShipping(false);
//        shoppingCartRepository.save(cart);
//
//        Cookie cookie = new Cookie(CART_TOKEN_COOKIE, token);
//        cookie.setPath("/");
//        cookie.setHttpOnly(true);
//        cookie.setMaxAge(COOKIE_MAX_AGE);
//        response.addCookie(cookie);
//
//        log.info("[CartTokenUtils] ĐÃ TẠO cart_token mới cho thao tác {} {}: {}", method, uri, token);
//        return token;
//    }
    public synchronized String getOrCreateCartToken(HttpServletRequest request, HttpServletResponse response) {
        // 1. Ưu tiên cart_token từ header nếu hợp lệ
        String headerToken = request.getHeader("cart_token");
        if (headerToken != null && !headerToken.trim().isEmpty()) {
            if (shoppingCartRepository.existsByToken(headerToken)) {
                log.debug("[CartTokenUtils] Dùng cart_token hợp lệ từ header: {}", headerToken);
                return headerToken;
            } else {
                log.warn("[CartTokenUtils] cart_token từ header không tồn tại trong DB: {}", headerToken);
            }
        }

        // 2. Nếu chưa có, thử lấy cart_token từ cookie nếu hợp lệ
        if (request.getCookies() != null) {
            for (Cookie cookie : request.getCookies()) {
                if (CART_TOKEN_COOKIE.equals(cookie.getName())) {
                    String cookieToken = cookie.getValue();
                    if (cookieToken != null && !cookieToken.trim().isEmpty()) {
                        if (shoppingCartRepository.existsByToken(cookieToken)) {
                            log.debug("[CartTokenUtils] Dùng cart_token hợp lệ từ cookie: {}", cookieToken);
                            return cookieToken;
                        } else {
                            log.warn("[CartTokenUtils] cart_token từ cookie không tồn tại trong DB: {}", cookieToken);
                        }
                    }
                }
            }
        }

        // 3. XÁC ĐỊNH request có cho phép tạo mới cart hay không?
        String method = request.getMethod();
        String uri = request.getRequestURI();
        boolean isCreateAction =
                ("POST".equalsIgnoreCase(method) || "PUT".equalsIgnoreCase(method))
                        && (uri.endsWith("/add") || uri.endsWith("/change")
                        || uri.endsWith("/attributes") || uri.endsWith("/note"));

        if (!isCreateAction) {
            log.warn("[CartTokenUtils] Không tạo mới cart_token vì không phải thao tác tạo/sửa giỏ hàng! Method: {}, URI: {}", method, uri);
            return null;
        }

        // 4. Tạo mới token và cart nếu đến đây
        String token = UUID.randomUUID().toString().replace("-", "");
        ShoppingCart cart = new ShoppingCart();
        cart.setToken(token);
        cart.setItemCount(0);
        cart.setTotalPrice(BigDecimal.ZERO);
        cart.setTotalWeight(BigDecimal.ZERO);
        cart.setRequiresShipping(false);
        shoppingCartRepository.save(cart);

        // 5. Tạo cookie chuẩn cho HTTPS + SPA
        ResponseCookie responseCookie = ResponseCookie.from(CART_TOKEN_COOKIE, token)
                .path("/")
                .httpOnly(true)
                .secure(true)         // ⚡ bắt buộc khi chạy HTTPS
                .sameSite("None")     // ⚡ cho phép frontend (Axios, React) gửi cookie kèm request
                .maxAge(COOKIE_MAX_AGE)
                .build();

        response.addHeader("Set-Cookie", responseCookie.toString());

        log.info("[CartTokenUtils] ĐÃ TẠO cart_token mới cho thao tác {} {}: {}", method, uri, token);
        return token;
    }

}
