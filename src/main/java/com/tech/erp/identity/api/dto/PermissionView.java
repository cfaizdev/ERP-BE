package com.tech.erp.identity.api.dto;

/** Published read model for one catalog entry. */
public record PermissionView(
        Long id,
        String code,
        String module,
        String action,
        String description) {
}
