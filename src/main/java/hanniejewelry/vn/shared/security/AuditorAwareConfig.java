package hanniejewelry.vn.shared.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

@Configuration
public class AuditorAwareConfig {

    @Bean
    public AuditorAware<String> auditorProvider() {
        // Demo: luôn trả "system", thực tế bạn lấy từ SecurityContext hoặc session
//        return () -> Optional.ofNullable("system");

//        Thực tế: Nếu dùng Spring Security, bạn sẽ lấy user hiện tại như sau:
        return () -> Optional.ofNullable(SecurityContextHolder.getContext().getAuthentication())
                .map(Authentication::getName);
    }
}
