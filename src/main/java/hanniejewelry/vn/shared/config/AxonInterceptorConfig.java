package hanniejewelry.vn.shared.config;

import hanniejewelry.vn.shared.interceptor.AuditCommandInterceptor;
import org.axonframework.commandhandling.CommandBus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AxonInterceptorConfig {
    @Autowired
    public void configure(CommandBus commandBus, AuditCommandInterceptor interceptor) {
        commandBus.registerDispatchInterceptor(interceptor);
    }
}
