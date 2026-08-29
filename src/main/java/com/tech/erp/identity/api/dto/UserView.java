package com.tech.erp.identity.api.dto;

import java.util.Set;
import java.util.UUID;

/** Published read model for a principal (ARCHITECTURE.md section 8.1). */
public record UserView(
        UUID id,
        String email,
        String status,
        Set<String> permissions,
        UUID companyId,
        UUID branchId) {
}
