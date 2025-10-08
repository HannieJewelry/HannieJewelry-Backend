package hanniejewelry.vn.user.caching.impl;

import lombok.RequiredArgsConstructor;
import hanniejewelry.vn.user.caching.InviteTokenStore;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class InviteTokenStoreImpl implements InviteTokenStore {
    private final StringRedisTemplate redisTemplate;

    @Override
    public void put(UUID userId, String token, Instant expiredAt) {
        long ttl = expiredAt.getEpochSecond() - Instant.now().getEpochSecond();
        redisTemplate.opsForValue().set("invite:" + userId, token, ttl, TimeUnit.SECONDS);
    }

    @Override
    public String get(UUID userId) {
        return redisTemplate.opsForValue().get("invite:" + userId);
    }

    @Override
    public void remove(UUID userId) {
        redisTemplate.delete("invite:" + userId);
    }
}
