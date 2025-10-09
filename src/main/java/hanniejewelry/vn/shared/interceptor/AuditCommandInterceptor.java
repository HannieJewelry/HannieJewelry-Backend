package hanniejewelry.vn.shared.interceptor;

import hanniejewelry.vn.user.entity.User;
import org.axonframework.messaging.MessageDispatchInterceptor;
import org.axonframework.commandhandling.CommandMessage;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;

@Component
public class AuditCommandInterceptor implements MessageDispatchInterceptor<CommandMessage<?>> {
    @Override
    public BiFunction<Integer, CommandMessage<?>, CommandMessage<?>> handle(List<? extends CommandMessage<?>> messages) {
        return (index, command) -> {
            Map<String, Object> metaData = new HashMap<>();
            String userId = getCurrentUserId();
            String userEmail = getCurrentUserEmail();

            if (userId != null) metaData.put("userId", userId);
            if (userEmail != null) metaData.put("userEmail", userEmail);

            return metaData.isEmpty() ? command : command.andMetaData(metaData);
        };
    }

    private String getCurrentUserId() {
        var auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.isAuthenticated()) {
            Object principal = auth.getPrincipal();
            if (principal instanceof User) {
                Object id = ((User) principal).getId();
                return id != null ? id.toString() : null;
            }
        }
        return null;
    }

    private String getCurrentUserEmail() {
        var auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.isAuthenticated()) {
            Object principal = auth.getPrincipal();
            if (principal instanceof UserDetails) {
                return ((UserDetails) principal).getUsername();
            }
            if (principal instanceof User) {
                return ((User) principal).getEmail();
            }
            return principal.toString();
        }
        return null;
    }
}
