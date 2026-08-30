package com.tech.erp.identity.application;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.Set;

/** Inbound commands for role administration. */
public final class RoleCommands {

    private RoleCommands() {
    }

    public record CreateRole(
            @NotBlank @Size(max = 40) String code,
            @NotBlank @Size(max = 120) String name,
            boolean system,
            Set<@NotBlank String> defaultPermissionCodes) {
    }

    /** Replaces the role's whole default set - existing users are not re-seeded. */
    public record SetDefaults(@NotNull Set<@NotBlank String> permissionCodes) {
    }
}
