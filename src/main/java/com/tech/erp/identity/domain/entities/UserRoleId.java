package com.tech.erp.identity.domain.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

/** Composite key of {@link UserRole} - {@code (user_id, role_id)}. */
@Embeddable
public class UserRoleId implements Serializable {

    @Column(name = "user_id")
    private Long userId;

    @Column(name = "role_id")
    private Long roleId;

    protected UserRoleId() {
        // populated by @MapsId from the owning associations
    }

    public Long userId() {
        return userId;
    }

    public Long roleId() {
        return roleId;
    }

    @Override
    public boolean equals(Object o) {
        return o instanceof UserRoleId other
                && Objects.equals(userId, other.userId)
                && Objects.equals(roleId, other.roleId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, roleId);
    }
}
