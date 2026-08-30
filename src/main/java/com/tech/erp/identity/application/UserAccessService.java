package com.tech.erp.identity.application;

import com.tech.erp.identity.api.dto.UserPermissionView;
import com.tech.erp.identity.application.UserAccessCommands.AssignRole;
import com.tech.erp.identity.application.UserAccessCommands.Decide;
import com.tech.erp.identity.domain.entities.GrantSource;
import com.tech.erp.identity.domain.entities.Permission;
import com.tech.erp.identity.domain.entities.Role;
import com.tech.erp.identity.domain.entities.User;
import com.tech.erp.identity.domain.entities.UserPermission;
import com.tech.erp.identity.domain.jpa.PermissionRepository;
import com.tech.erp.identity.domain.jpa.RoleRepository;
import com.tech.erp.identity.domain.jpa.UserRepository;

import java.util.Comparator;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Changes one user's access - the per-user half of the model.
 *
 * <p>{@link #assignRole} is the RBAC path; {@link #decide} and {@link #revoke} are
 * the customization path. Both write to the same authoritative grant list, and the
 * aggregate decides how they interact.
 */
@Service
@Transactional
public class UserAccessService {

    private final UserRepository users;
    private final RoleRepository roles;
    private final PermissionRepository permissions;

    UserAccessService(UserRepository users, RoleRepository roles, PermissionRepository permissions) {
        this.users = users;
        this.roles = roles;
        this.permissions = permissions;
    }

    /** Seeds the role's defaults onto the user, leaving manual decisions alone. */
    public void assignRole(Long userId, AssignRole command) {
        User user = load(userId);
        String roleCode = Permission.normalizeCode(command.roleCode(), "role code");
        // Lazy defaults are fine here - we are inside the transaction, and the entity
        // graph variant exists only for reads that escape it.
        Role role = roles.findByCode(roleCode).orElseThrow(() -> new RoleNotFoundException(roleCode));

        user.assignRole(role, command.assignedBy());
        users.save(user);
    }

    /** Grants or explicitly denies a single permission, overriding any role default. */
    public void decide(Long userId, String permissionCode, Decide command) {
        User user = load(userId);
        Permission permission = resolve(permissionCode);
        if (command.granted()) {
            user.grant(permission, GrantSource.MANUAL, command.decidedBy());
        } else {
            user.deny(permission, command.decidedBy());
        }
        users.save(user);
    }

    /**
     * Removes the decision entirely. Unlike a deny this leaves nothing on record, so
     * a later role assignment is free to seed the permission again.
     */
    public void revoke(Long userId, String permissionCode) {
        User user = load(userId);
        user.revoke(resolve(permissionCode));
        users.save(user);
    }

    /** Every decision on record for the user, denies included, newest first. */
    @Transactional(readOnly = true)
    public List<UserPermissionView> grantsOf(Long userId) {
        return load(userId).grants().stream()
                .sorted(Comparator.comparing(UserPermission::permissionCode))
                .map(UserAccessService::toView)
                .toList();
    }

    private User load(Long userId) {
        return users.findWithAccessById(userId).orElseThrow(() -> new UserNotFoundException(userId));
    }

    private Permission resolve(String permissionCode) {
        String normalized = Permission.normalizeCode(permissionCode, "permission code");
        return permissions.findByCode(normalized)
                .orElseThrow(() -> new PermissionNotFoundException(normalized));
    }

    private static UserPermissionView toView(UserPermission grant) {
        return new UserPermissionView(
                grant.permissionCode(),
                grant.isGranted(),
                grant.source().name(),
                grant.grantedBy(),
                grant.grantedAt());
    }
}
