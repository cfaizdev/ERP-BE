package com.tech.erp.identity.api.dto;

import java.time.Instant;

/**
 * One recorded decision on one permission for one user.
 *
 * <p>Unlike {@link UserView#permissions()} this includes denies, so an
 * administration screen can tell "never granted" apart from "explicitly taken
 * away", and show whether the decision came from a role or a person.
 */
public record UserPermissionView(
        String permissionCode,
        boolean granted,
        String source,
        Long grantedBy,
        Instant grantedAt) {
}
