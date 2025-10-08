package hanniejewelry.vn.shared.config;

import org.axonframework.config.EventProcessingConfigurer;
import org.axonframework.serialization.Serializer;
import org.axonframework.serialization.json.JacksonSerializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
public class AxonConfig {
    @Bean
    @Primary
    public Serializer serializer() {
        return JacksonSerializer.defaultSerializer();
    }
    
    @Autowired
    public void configure(EventProcessingConfigurer configurer) {
        configurer.registerTrackingEventProcessor("hanniejewelry.vn.notification.handler");
    }
}
