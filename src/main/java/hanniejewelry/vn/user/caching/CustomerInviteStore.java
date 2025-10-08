package hanniejewelry.vn.user.caching;

import java.time.Instant;
import java.util.UUID;

public interface CustomerInviteStore {
    void storeCustomerRelationship(UUID userId, UUID customerId, Instant expiredAt);
    UUID getCustomerId(UUID userId);
    void removeCustomerRelationship(UUID userId);
} 