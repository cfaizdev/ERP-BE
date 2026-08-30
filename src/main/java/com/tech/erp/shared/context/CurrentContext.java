package com.tech.erp.shared.context;

import java.util.Optional;
import java.util.UUID;

/**
 * Who is acting, and on behalf of which company - the one place the rest of the
 * application asks.
 *
 * <p>This is the seam that keeps auditing and tenancy out of the domain model.
 * Entities never resolve the actor themselves; {@code AuditorAware} and the
 * company stamping listener read it here, so switching from "nobody is
 * authenticated" to "read the JWT principal" is a change to one implementation
 * class and nothing else.
 *
 * <p>Both values are optional on purpose. Background work, event listeners and
 * startup seeding have no request and no principal, and must not blow up.
 */
public interface CurrentContext {

    /** The acting principal's id, or empty when nothing is authenticated. */
    Optional<Long> userId();

    /** The company the current work belongs to, or empty when unresolved. */
    Optional<UUID> companyId();
}
