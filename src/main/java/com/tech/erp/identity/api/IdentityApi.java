package com.tech.erp.identity.api;

import com.tech.erp.identity.api.dto.UserView;
import java.util.Optional;
import java.util.Set;

/**
 * Published contract of the Identity &amp; Access module (ARCHITECTURE.md section 8.1).
 *
 * <p>This is the seam that becomes a REST/gRPC boundary if the module is ever
 * extracted. Callers ask authorization questions here; enforcement and the user
 * model stay inside the module. Administering roles and permissions is
 * deliberately absent - that is this module's own business, not another module's.
 */
public interface IdentityApi {

    Optional<UserView> findUser(Long userId);

    /** e.g. {@code hasPermission(userId, "ORDER_CREATE")}. Honours explicit denies. */
    boolean hasPermission(Long userId, String permissionCode);

    Set<String> permissionsOf(Long userId);
}
