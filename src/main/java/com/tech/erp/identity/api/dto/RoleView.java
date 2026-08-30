package com.tech.erp.identity.api.dto;

import java.util.Set;

/** Published read model for a role and the default grants it hands out. */
public record RoleView(
        Long id,
        String code,
        String name,
        boolean system,
        Set<String> defaultPermissions) {
}
