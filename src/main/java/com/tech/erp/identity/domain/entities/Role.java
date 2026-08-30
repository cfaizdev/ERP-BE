package com.tech.erp.identity.domain.entities;

import com.tech.erp.shared.domain.AuditableEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * A named template of default grants.
 *
 * <p>A role is not consulted at access-check time - assigning one only seeds
 * {@link GrantSource#ROLE_DEFAULT} rows into the user's authoritative grant list.
 * Editing a role's defaults therefore does not retroactively change anyone's
 * access until the role is assigned again.
 */
@Entity
@Table(name = "roles", schema = "identity")
public class Role extends AuditableEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "role_id")
    private Long id;

    @Column(name = "role_code", nullable = false, unique = true, length = 40)
    private String code;

    @Column(name = "role_name", nullable = false, length = 120)
    private String name;

    @Column(name = "is_system", nullable = false)
    private boolean system;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "role_permissions", schema = "identity",
            joinColumns = @JoinColumn(name = "role_id"),
            inverseJoinColumns = @JoinColumn(name = "permission_id"))
    private Set<Permission> defaultPermissions = new LinkedHashSet<>();

    protected Role() {
        // for JPA
    }

    private Role(String code, String name, boolean system) {
        this.code = code;
        this.name = name;
        this.system = system;
    }

    public static Role define(String code, String name, boolean system) {
        Objects.requireNonNull(name, "role name must not be null");
        if (name.isBlank()) {
            throw new IllegalArgumentException("role name must not be blank");
        }
        return new Role(Permission.normalizeCode(code, "role code"), name.trim(), system);
    }

    public void addDefault(Permission permission) {
        defaultPermissions.add(Objects.requireNonNull(permission, "permission must not be null"));
    }

    public void removeDefault(Permission permission) {
        defaultPermissions.remove(permission);
    }

    public void replaceDefaults(Collection<Permission> permissions) {
        defaultPermissions.clear();
        permissions.forEach(this::addDefault);
    }

    public Long id() {
        return id;
    }

    public String code() {
        return code;
    }

    public String name() {
        return name;
    }

    public boolean isSystem() {
        return system;
    }

    public Set<Permission> defaultPermissions() {
        return Set.copyOf(defaultPermissions);
    }

    public Set<String> defaultPermissionCodes() {
        return defaultPermissions.stream().map(Permission::code).collect(Collectors.toCollection(LinkedHashSet::new));
    }

    @Override
    public boolean equals(Object o) {
        return o instanceof Role other && Objects.equals(code, other.code);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(code);
    }

    @Override
    public String toString() {
        return code;
    }
}
