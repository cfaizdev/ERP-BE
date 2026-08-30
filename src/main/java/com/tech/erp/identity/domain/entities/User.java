package com.tech.erp.identity.domain.entities;

import com.tech.erp.identity.api.events.UserRegistered;
import com.tech.erp.shared.domain.AuditableAggregateRoot;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.time.Instant;
import java.util.LinkedHashSet;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * A principal who can authenticate (ARCHITECTURE.md section 8.1).
 *
 * <p>
 * Aggregate root: registration, status changes and permission grants all go
 * through this type so policy lives in one place. Company/branch are referenced
 * by id, never as objects - {@code companyId} is inherited from
 * {@link AuditableAggregateRoot}, since every entity carries it.
 *
 * <p>
 * Access is decided by two mechanisms that meet here. Assigning a {@link Role}
 * seeds a {@link GrantSource#ROLE_DEFAULT} row per role default (RBAC); granting
 * or denying a single {@link Permission} writes a {@link GrantSource#MANUAL} row
 * (per-user customization). A manual row always wins, and re-assigning the role
 * will not overwrite it - so a user can keep a role while being denied one of the
 * permissions it hands out.
 */
@Entity
@Table(name = "users", schema = "identity")
public class User extends AuditableAggregateRoot<User> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    @Embedded
    private Email email;

    @Column(name = "password_hash", nullable = false)
    private String passwordHash;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private UserStatus status;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private Set<UserPermission> permissions = new LinkedHashSet<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private Set<UserRole> roles = new LinkedHashSet<>();

    @Column(name = "branch_id")
    private UUID branchId;

    protected User() {
        // for JPA
    }

    private User(Email email, String passwordHash, UUID companyId, UUID branchId) {
        this.email = email;
        this.passwordHash = passwordHash;
        this.branchId = branchId;
        this.status = UserStatus.ACTIVE;
        // Registration always names the company, so the stamping listener never
        // has to guess one for a user.
        assignCompany(companyId);
    }

    public static User register(String email, String passwordHash, UUID companyId, UUID branchId) {
        return new User(new Email(email), passwordHash, companyId, branchId);
    }

    /**
     * Registers the {@link UserRegistered} event. Called by the application service
     * <em>after</em> the first save: the id is database-generated, so an event built
     * inside {@link #register} would carry a null {@code userId} to every listener.
     */
    public void markRegistered() {
        registerEvent(new UserRegistered(id, email.value(), Instant.now()));
    }

    /** Allows {@code permission}, recording who decided and under which mechanism. */
    public void grant(Permission permission, GrantSource source, Long grantedBy) {
        decide(permission, true, source, grantedBy);
    }

    /** Explicit deny - always {@link GrantSource#MANUAL}, and survives role re-assignment. */
    public void deny(Permission permission, Long deniedBy) {
        decide(permission, false, GrantSource.MANUAL, deniedBy);
    }

    /** Drops the row entirely, so the permission falls back to "not granted, no decision on record". */
    public void revoke(Permission permission) {
        permissions.removeIf(grant -> grant.isFor(permission));
    }

    /**
     * Records the role and seeds its defaults. Existing {@link GrantSource#MANUAL}
     * rows are left untouched - that is the whole point of the source column.
     */
    public void assignRole(Role role, Long assignedBy) {
        if (roles.stream().noneMatch(assigned -> assigned.isFor(role))) {
            roles.add(new UserRole(this, role, assignedBy));
        }
        for (Permission permission : role.defaultPermissions()) {
            Optional<UserPermission> existing = find(permission);
            if (existing.isEmpty()) {
                permissions.add(new UserPermission(this, permission, true, GrantSource.ROLE_DEFAULT, assignedBy));
            } else if (existing.get().source() == GrantSource.ROLE_DEFAULT) {
                existing.get().decide(true, GrantSource.ROLE_DEFAULT, assignedBy);
            }
        }
    }

    public void disable() {
        this.status = UserStatus.DISABLED;
    }

    public boolean canAuthenticate() {
        return status == UserStatus.ACTIVE;
    }

    /** True only when a row exists for the code <em>and</em> it is an allow. */
    public boolean hasPermission(String permissionCode) {
        return permissions.stream()
                .anyMatch(grant -> grant.isGranted() && grant.permissionCode().equals(permissionCode));
    }

    private void decide(Permission permission, boolean granted, GrantSource source, Long actor) {
        find(permission).ifPresentOrElse(
                existing -> existing.decide(granted, source, actor),
                () -> permissions.add(new UserPermission(this, permission, granted, source, actor)));
    }

    private Optional<UserPermission> find(Permission permission) {
        return permissions.stream().filter(grant -> grant.isFor(permission)).findFirst();
    }

    public Long id() {
        return id;
    }

    public String emailValue() {
        return email.value();
    }

    public String statusName() {
        return status.name();
    }

    /** The effective permission codes - granted rows only. */
    public Set<String> permissions() {
        return permissions.stream()
                .filter(UserPermission::isGranted)
                .map(UserPermission::permissionCode)
                .collect(Collectors.toCollection(LinkedHashSet::new));
    }

    /** Every decision on record, denies included - for the administration screens. */
    public Set<UserPermission> grants() {
        return Set.copyOf(permissions);
    }

    public Set<UserRole> assignedRoles() {
        return Set.copyOf(roles);
    }

    public UUID branchId() {
        return branchId;
    }
}
