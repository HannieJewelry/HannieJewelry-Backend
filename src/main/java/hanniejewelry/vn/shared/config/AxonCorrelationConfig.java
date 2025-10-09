package hanniejewelry.vn.shared.config;

import org.axonframework.messaging.correlation.CorrelationDataProvider;
import org.axonframework.messaging.correlation.SimpleCorrelationDataProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AxonCorrelationConfig {
    @Bean
    public CorrelationDataProvider userIdCorrelationDataProvider() {
        return new SimpleCorrelationDataProvider("userId", "userEmail");
    }
}
