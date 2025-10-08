package hanniejewelry.vn.user.caching;

import java.time.Instant;

public interface OtpTempStore {
    void put(String phone, String otp, Instant expiredAt);
    OtpEntry get(String phone);
    void remove(String phone);

    record OtpEntry(String otp, Instant expiredAt) {

    }
}
