package hanniejewelry.vn.user.caching.impl;

import lombok.RequiredArgsConstructor;
import hanniejewelry.vn.user.caching.OtpTempStore;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class OtpTempStoreImpl implements OtpTempStore {
    private final StringRedisTemplate redisTemplate;

    @Override
    public void put(String phone, String otp, Instant expiredAt) {
        long ttl = expiredAt.getEpochSecond() - Instant.now().getEpochSecond();
        redisTemplate.opsForValue().set("otp:" + phone, otp, ttl, TimeUnit.SECONDS);
    }

    @Override
    public OtpEntry get(String phone) {
        String value = redisTemplate.opsForValue().get("otp:" + phone);
        if (value == null) return null;
        return new OtpEntry(value, null);
    }

    @Override
    public void remove(String phone) {
        redisTemplate.delete("otp:" + phone);
    }
}
