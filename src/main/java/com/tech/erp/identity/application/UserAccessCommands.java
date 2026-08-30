package com.tech.erp.identity.application;

import jakarta.validation.constraints.NotBlank;

/** Inbound commands for changing one user's access. */
public final class UserAccessCommands {

    private UserAccessCommands() {
    }

    public record AssignRole(@NotBlank String roleCode, Long assignedBy) {
    }

    /**
     * One deliberate decision about one permission. {@code granted = false} is an
     * explicit deny, not a removal - it is recorded and outranks the role default.
     */
    public record Decide(boolean granted, Long decidedBy) {
    }
}
