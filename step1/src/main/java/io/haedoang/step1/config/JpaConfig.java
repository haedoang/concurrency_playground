package io.haedoang.step1.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing(auditorAwareRef = "customAuditorAware")
@Configuration
public class JpaConfig {
}
