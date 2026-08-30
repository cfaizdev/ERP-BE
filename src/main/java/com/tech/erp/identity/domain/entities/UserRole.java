package com.tech.erp.identity.domain.entities;

import com.tech.erp.shared.domain.AuditableEntity;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.Table;
import java.time.Instant;
import java.util.Objects;

/**
 * Which role a user was created under. Kept for reference and re-seeding only -
 * no access check reads this table, it exists so the origin of a user's
 * {@link GrantSource#ROLE_DEFAULT} rows stays auditable.
 *
 * <p>Part of the {@link User} aggregate.
 */
@Entity
@Table(name = "user_roles", schema = "identity")
public class UserRole extends AuditableEntity {

    @EmbeddedId
    private UserRoleId id = new UserRoleId();

    @MapsId("userId")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id")
    private User user;

    @MapsId("roleId")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "role_id")
    private Role role;

    @Column(name = "assigned_by")
    private Long assignedBy;

    @Column(name = "assigned_at", nullable = false)
    private Instant assignedAt;

    protected UserRole() {
        // for JPA
    }

    UserRole(User user, Role role, Long assignedBy) {
        this.user = user;
        this.role = role;
        this.assignedBy = assignedBy;
        this.assignedAt = Instant.now();
        // An assignment belongs to whichever company its user does - see UserPermission.
        assignCompany(user.companyId());
    }

    /** Compared through the accessor, which initializes a lazy proxy - direct field access would not. */
    boolean isFor(Role other) {
        return role.code().equals(other.code());
    }

    public Role role() {
        return role;
    }

    public String roleCode() {
        return role.code();
    }

    public Long assignedBy() {
        return assignedBy;
    }

    public Instant assignedAt() {
        return assignedAt;
    }

    @Override
    public boolean equals(Object o) {
        return o instanceof UserRole other && Objects.equals(role, other.role);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(role);
    }
}
