package com.tech.erp.identity.domain;

import com.tech.erp.identity.api.events.UserRegistered;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Table;
import java.time.Instant;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.UUID;
import org.springframework.data.domain.AbstractAggregateRoot;

/**
 * A principal who can authenticate (ARCHITECTURE.md section 8.1).
 *
 * <p>Aggregate root: registration, status changes and permission grants all go
 * through this type so policy lives in one place. Company/branch are referenced
 * by id, never as objects.
 */
@Entity
@Table(name = "users", schema = "identity")
public class User extends AbstractAggregateRoot<User> {

    @Id
    private UUID id;

    @Embedded
    private Email email;

    @Column(name = "password_hash", nullable = false)
    private String passwordHash;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UserStatus status;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(
            name = "user_permissions",
            schema = "identity",
            joinColumns = @JoinColumn(name = "user_id"))
    @Column(name = "permission")
    private Set<String> permissions = new LinkedHashSet<>();

    private UUID companyId;
    private UUID branchId;

    protected User() {
        // for JPA
    }

    private User(UUID id, Email email, String passwordHash, UUID companyId, UUID branchId) {
        this.id = id;
        this.email = email;
        this.passwordHash = passwordHash;
        this.companyId = companyId;
        this.branchId = branchId;
        this.status = UserStatus.ACTIVE;
    }

    public static User register(String email, String passwordHash, UUID companyId, UUID branchId) {
        User user = new User(UUID.randomUUID(), new Email(email), passwordHash, companyId, branchId);
        user.registerEvent(new UserRegistered(user.id, user.email.value(), Instant.now()));
        return user;
    }

    public void grant(String permission) {
        this.permissions.add(permission);
    }

    public void disable() {
        this.status = UserStatus.DISABLED;
    }

    public boolean canAuthenticate() {
        return status == UserStatus.ACTIVE;
    }

    public boolean hasPermission(String permission) {
        return permissions.contains(permission);
    }

    public UUID id() {
        return id;
    }

    public String emailValue() {
        return email.value();
    }

    public String statusName() {
        return status.name();
    }

    public Set<String> permissions() {
        return Set.copyOf(permissions);
    }

    public UUID companyId() {
        return companyId;
    }

    public UUID branchId() {
        return branchId;
    }
}
