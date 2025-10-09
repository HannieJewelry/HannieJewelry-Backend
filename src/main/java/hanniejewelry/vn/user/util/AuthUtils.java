package hanniejewelry.vn.user.util;

import org.springframework.stereotype.Component;

import java.util.Random;

@Component
public class AuthUtils {
    private AuthUtils() {}

    public static String normalizePhone(String phone) {
        return phone.startsWith("+") ? phone : "+84" + phone.replaceFirst("^0", "");
    }

    public static String genOtp() {
        return String.format("%06d", new Random().nextInt(1_000_000));
    }
}
