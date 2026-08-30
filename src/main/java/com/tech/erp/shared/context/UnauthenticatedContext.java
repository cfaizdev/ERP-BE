package com.tech.erp.shared.context;

import java.util.Optional;
import java.util.UUID;
import org.springframework.stereotype.Component;

/**
 * The placeholder implementation: nobody is authenticated, so nothing is known.
 *
 * <p>There is no Spring Security filter chain yet (ARCHITECTURE.md section 8.1 -
 * only {@code spring-security-crypto} is on the classpath, for BCrypt). Until
 * there is, {@code created_by} / {@code updated_by} stay null and {@code company_id}
 * is only ever set by an aggregate that was handed one explicitly.
 *
 * <p>This class is the entire cost of turning that on later: replace the two method
 * bodies with reads off the authenticated principal. No entity, service or
 * controller changes.
 */
@Component
class UnauthenticatedContext implements CurrentContext {

    @Override
    public Optional<Long> userId() {
        return Optional.empty();
    }

    @Override
    public Optional<UUID> companyId() {
        return Optional.empty();
    }
}
