package com.tech.erp.identity.domain.entities;

import com.tech.erp.shared.domain.AuditableEntity;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.Table;
import java.time.Instant;
import java.util.Objects;

/**
 * The authoritative decision for one permission on one user. Every access check
 * reads only these rows.
 *
 * <p>A row is an allow when {@code granted}, and an explicit deny otherwise - the
 * absence of a row and a deny row are different things, which is what makes it
 * possible to take one permission away from a user without touching their role.
 *
 * <p>Part of the {@link User} aggregate: only {@code User} creates or decides
 * these, so the "manual beats role default" policy lives in exactly one place.
 */
@Entity
@Table(name = "user_permissions", schema = "identity",
        indexes = @Index(name = "idx_user_perms_user", columnList = "user_id"))
public class UserPermission extends AuditableEntity {

    @EmbeddedId
    private UserPermissionId id = new UserPermissionId();

    @MapsId("userId")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id")
    private User user;

    @MapsId("permissionId")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "permission_id")
    private Permission permission;

    @Column(name = "is_granted", nullable = false)
    private boolean granted;

    @Enumerated(EnumType.STRING)
    @Column(name = "source", nullable = false, length = 15)
    private GrantSource source;

    @Column(name = "granted_by")
    private Long grantedBy;

    @Column(name = "granted_at", nullable = false)
    private Instant grantedAt;

    protected UserPermission() {
        // for JPA
    }

    UserPermission(User user, Permission permission, boolean granted, GrantSource source, Long grantedBy) {
        this.user = user;
        this.permission = permission;
        // A grant belongs to whichever company its user does, so it never depends on
        // an ambient company being resolvable at the moment it is written.
        assignCompany(user.companyId());
        decide(granted, source, grantedBy);
    }

    /** Overwrites the decision, re-stamping provenance and time. */
    void decide(boolean granted, GrantSource source, Long grantedBy) {
        this.granted = granted;
        this.source = Objects.requireNonNull(source, "source must not be null");
        this.grantedBy = grantedBy;
        this.grantedAt = Instant.now();
    }

    /** Compared through the accessor, which initializes a lazy proxy - direct field access would not. */
    boolean isFor(Permission other) {
        return permission.code().equals(other.code());
    }

    public Permission permission() {
        return permission;
    }

    public String permissionCode() {
        return permission.code();
    }

    public boolean isGranted() {
        return granted;
    }

    public GrantSource source() {
        return source;
    }

    public Long grantedBy() {
        return grantedBy;
    }

    public Instant grantedAt() {
        return grantedAt;
    }

    /**
     * Keyed on the permission rather than the {@link UserPermissionId}, which stays
     * null until flush - these live in a {@code Set} on the aggregate from the
     * moment they are created.
     */
    @Override
    public boolean equals(Object o) {
        return o instanceof UserPermission other && Objects.equals(permission, other.permission);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(permission);
    }
}
