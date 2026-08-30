package com.tech.erp.identity.api.dto;

import java.util.Set;
import java.util.UUID;

/**
 * Published read model for a principal (ARCHITECTURE.md section 8.1).
 *
 * <p>{@code permissions} holds the effective codes - granted only. A permission
 * that was explicitly denied is simply absent; see {@link UserPermissionView} for
 * the decisions themselves.
 */
public record UserView(
        Long id,
        String email,
        String status,
        Set<String> permissions,
        UUID companyId,
        UUID branchId) {
}
