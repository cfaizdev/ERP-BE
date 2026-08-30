package com.tech.erp.shared.domain;

import com.tech.erp.shared.context.CurrentContext;

import jakarta.persistence.PrePersist;
import org.springframework.stereotype.Component;

/**
 * Fills in {@code company_id} on insert for rows that were not handed one.
 *
 * <p>The counterpart to Spring Data's {@code AuditingEntityListener}, which does
 * the same job for the timestamps and the actor. Kept separate because tenancy is
 * ours, not Spring's.
 *
 * <p>Insert only. A row does not change company after it is written, and letting an
 * update rewrite the column would silently move data between tenants the moment a
 * background job with no resolved company touched it.
 *
 * <p>Injected rather than instantiated by Hibernate: Spring Boot registers a
 * {@code SpringBeanContainer}, so a bean-backed entity listener resolves normally.
 */
@Component
public class CompanyStampingListener {

    private final CurrentContext context;

    CompanyStampingListener(CurrentContext context) {
        this.context = context;
    }

    @PrePersist
    void stampCompany(AuditableEntity entity) {
        entity.stampCompanyIfAbsent(context.companyId().orElse(null));
    }
}
