package hanniejewelry.vn.shared.config;

import hanniejewelry.vn.shared.filter.GenericBlazeFilterApplier;
import hanniejewelry.vn.product.entity.Product;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BlazeFilterConfig {

    @Bean
    public GenericBlazeFilterApplier<Product> productBlazeFilterApplier() {
        return new GenericBlazeFilterApplier<>();
    }

}
