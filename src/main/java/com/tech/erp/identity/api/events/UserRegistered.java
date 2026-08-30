package com.tech.erp.identity.api.events;

import java.time.Instant;

/**
 * Published when a new principal is registered (ARCHITECTURE.md section 8.1).
 *
 * <p>In the monolith this is an in-process event backed by Modulith's event
 * publication registry; on extraction it flows over a broker unchanged.
 */
public record UserRegistered(Long userId, String email, Instant registeredAt) {
}
