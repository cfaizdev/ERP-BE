package com.tech.erp.identity.application;

import com.tech.erp.identity.domain.entities.PermissionAction;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/** Inbound commands for the permission catalog. */
public final class PermissionCommands {

    private PermissionCommands() {
    }

    public record CreatePermission(
            @NotBlank @Size(max = 60) String code,
            @NotBlank @Size(max = 40) String module,
            @NotNull PermissionAction action,
            @Size(max = 200) String description) {
    }
}
