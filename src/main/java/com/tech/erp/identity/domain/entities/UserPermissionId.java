package com.tech.erp.identity.domain.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

/** Composite key of {@link UserPermission} - {@code (user_id, permission_id)}. */
@Embeddable
public class UserPermissionId implements Serializable {

    @Column(name = "user_id")
    private Long userId;

    @Column(name = "permission_id")
    private Long permissionId;

    protected UserPermissionId() {
        // populated by @MapsId from the owning associations
    }

    public Long userId() {
        return userId;
    }

    public Long permissionId() {
        return permissionId;
    }

    @Override
    public boolean equals(Object o) {
        return o instanceof UserPermissionId other
                && Objects.equals(userId, other.userId)
                && Objects.equals(permissionId, other.permissionId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, permissionId);
    }
}
