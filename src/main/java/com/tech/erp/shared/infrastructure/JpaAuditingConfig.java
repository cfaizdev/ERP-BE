package com.tech.erp.shared.infrastructure;

import com.tech.erp.shared.context.CurrentContext;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

/**
 * Turns on Spring Data JPA auditing for the whole application - the
 * {@code @CreatedDate} / {@code @LastModifiedDate} / {@code @CreatedBy} /
 * {@code @LastModifiedBy} fields on {@code AuditableEntity}.
 *
 * <p>The auditor is read through {@code CurrentContext}, so this class does not
 * change when authentication arrives.
 */
@Configuration
@EnableJpaAuditing(auditorAwareRef = "currentUserAuditor")
class JpaAuditingConfig {

    @Bean
    AuditorAware<Long> currentUserAuditor(CurrentContext context) {
        return context::userId;
    }
}
