package com.tech.erp.identity.domain.entities;

import com.tech.erp.shared.domain.AuditableEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import java.util.Objects;

/**
 * One grantable capability - the catalog every access check is expressed against.
 *
 * <p>Adding a new grant is an INSERT here, never a schema change. This is a small
 * reference aggregate: it is created and read, never mutated in place, so a code
 * once published keeps meaning the same thing for every row that points at it.
 */
@Entity
@Table(name = "permissions", schema = "identity",
        uniqueConstraints = @UniqueConstraint(
                name = "uk_permissions_module_action", columnNames = {"module", "action"}))
public class Permission extends AuditableEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "permission_id")
    private Long id;

    @Column(name = "permission_code", nullable = false, unique = true, length = 60)
    private String code;

    @Column(name = "module", nullable = false, length = 40)
    private String module;

    @Enumerated(EnumType.STRING)
    @Column(name = "action", nullable = false, length = 20)
    private PermissionAction action;

    @Column(name = "description", length = 200)
    private String description;

    protected Permission() {
        // for JPA
    }

    private Permission(String code, String module, PermissionAction action, String description) {
        this.code = code;
        this.module = module;
        this.action = action;
        this.description = description;
    }

    public static Permission define(String code, String module, PermissionAction action, String description) {
        return new Permission(
                normalizeCode(code, "permission code"),
                normalizeCode(module, "module"),
                Objects.requireNonNull(action, "action must not be null"),
                description == null || description.isBlank() ? null : description.trim());
    }

    /** Codes are upper-cased on the way in so {@code user_create} and {@code USER_CREATE} are one row. */
    public static String normalizeCode(String value, String what) {
        Objects.requireNonNull(value, () -> what + " must not be null");
        String normalized = value.trim().toUpperCase();
        if (normalized.isEmpty()) {
            throw new IllegalArgumentException(what + " must not be blank");
        }
        return normalized;
    }

    public Long id() {
        return id;
    }

    public String code() {
        return code;
    }

    public String module() {
        return module;
    }

    public PermissionAction action() {
        return action;
    }

    public String description() {
        return description;
    }

    /** Keyed on the immutable natural key so instances behave in a {@code Set} before they have an id. */
    @Override
    public boolean equals(Object o) {
        return o instanceof Permission other && Objects.equals(code, other.code);
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
