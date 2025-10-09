package hanniejewelry.vn.user.caching;


import java.time.Instant;
import java.util.UUID;

public interface InviteTokenStore {
    void put(UUID userId, String token, Instant expiredAt);
    String get(UUID userId);
    void remove(UUID userId);
}
