package com.tech.erp.identity.api;

import com.tech.erp.identity.api.dto.UserView;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

/**
 * Published contract of the Identity &amp; Access module (ARCHITECTURE.md section 8.1).
 *
 * <p>This is the seam that becomes a REST/gRPC boundary if the module is ever
 * extracted. Callers ask authorization questions here; enforcement and the user
 * model stay inside the module.
 */
public interface IdentityApi {

    Optional<UserView> findUser(UUID userId);

    /** e.g. {@code hasPermission(userId, "sales:order:create")}. */
    boolean hasPermission(UUID userId, String permission);

    Set<String> permissionsOf(UUID userId);
}
