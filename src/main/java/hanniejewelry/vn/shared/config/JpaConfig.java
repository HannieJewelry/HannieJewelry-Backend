package hanniejewelry.vn.shared.config;

import hanniejewelry.vn.shared.infrastructure.impl.BaseRepositoryImpl;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@EnableJpaRepositories(
        basePackages = "hanniejewelry.vn",
        repositoryBaseClass = BaseRepositoryImpl.class
)

@Configuration
public class JpaConfig {}
