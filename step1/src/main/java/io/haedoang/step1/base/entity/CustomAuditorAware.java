package io.haedoang.step1.base.entity;

import org.springframework.data.domain.AuditorAware;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class CustomAuditorAware implements AuditorAware<String> {

    public static final String DEFAULT_AUDITOR = "SYSTEM";

    @Override
    public Optional<String> getCurrentAuditor() {
        return Optional.of(DEFAULT_AUDITOR);
    }
}
