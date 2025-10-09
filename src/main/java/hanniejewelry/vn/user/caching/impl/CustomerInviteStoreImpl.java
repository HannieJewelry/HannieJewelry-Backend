package hanniejewelry.vn.user.caching.impl;

import hanniejewelry.vn.user.caching.CustomerInviteStore;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class CustomerInviteStoreImpl implements CustomerInviteStore {
    private final StringRedisTemplate redisTemplate;
    private static final String CUSTOMER_INVITE_KEY = "customer_invite:";

    @Override
    public void storeCustomerRelationship(UUID userId, UUID customerId, Instant expiredAt) {
        long ttl = expiredAt.getEpochSecond() - Instant.now().getEpochSecond();
        redisTemplate.opsForValue().set(CUSTOMER_INVITE_KEY + userId, customerId.toString(), ttl, TimeUnit.SECONDS);
    }

    @Override
    public UUID getCustomerId(UUID userId) {
        String customerIdStr = redisTemplate.opsForValue().get(CUSTOMER_INVITE_KEY + userId);
        return customerIdStr != null ? UUID.fromString(customerIdStr) : null;
    }

    @Override
    public void removeCustomerRelationship(UUID userId) {
        redisTemplate.delete(CUSTOMER_INVITE_KEY + userId);
    }
} 