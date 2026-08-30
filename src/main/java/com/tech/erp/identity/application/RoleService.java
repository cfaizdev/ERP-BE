package com.tech.erp.identity.application;

import com.tech.erp.identity.api.dto.RoleView;
import com.tech.erp.identity.application.RoleCommands.CreateRole;
import com.tech.erp.identity.application.RoleCommands.SetDefaults;
import com.tech.erp.identity.domain.entities.Permission;
import com.tech.erp.identity.domain.entities.Role;
import com.tech.erp.identity.domain.jpa.PermissionRepository;
import com.tech.erp.identity.domain.jpa.RoleRepository;

import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Maintains roles - the default-grant templates.
 *
 * <p>Editing a role's defaults deliberately does <em>not</em> touch users who
 * already hold it. Grants only move when a role is assigned, which keeps the
 * authoritative per-user list the single thing an access check has to read.
 */
@Service
@Transactional
public class RoleService {

    private final RoleRepository roles;
    private final PermissionRepository permissions;

    RoleService(RoleRepository roles, PermissionRepository permissions) {
        this.roles = roles;
        this.permissions = permissions;
    }

    public Long create(CreateRole command) {
        String code = Permission.normalizeCode(command.code(), "role code");
        if (roles.existsByCode(code)) {
            throw new DuplicateRoleCodeException(code);
        }
        Role role = Role.define(code, command.name(), command.system());
        if (command.defaultPermissionCodes() != null) {
            role.replaceDefaults(resolve(command.defaultPermissionCodes()));
        }
        return roles.save(role).id();
    }

    public void setDefaults(Long roleId, SetDefaults command) {
        Role role = roles.findWithDefaultsById(roleId).orElseThrow(() -> new RoleNotFoundException(roleId));
        role.replaceDefaults(resolve(command.permissionCodes()));
        roles.save(role);
    }

    @Transactional(readOnly = true)
    public List<RoleView> findAll() {
        return roles.findAll().stream().map(RoleService::toView).toList();
    }

    @Transactional(readOnly = true)
    public Optional<RoleView> findById(Long id) {
        return roles.findWithDefaultsById(id).map(RoleService::toView);
    }

    /** Resolves codes to catalog entries, failing on the first unknown one. */
    private Set<Permission> resolve(Collection<String> codes) {
        Set<Permission> resolved = new LinkedHashSet<>();
        for (String code : codes) {
            String normalized = Permission.normalizeCode(code, "permission code");
            resolved.add(permissions.findByCode(normalized)
                    .orElseThrow(() -> new PermissionNotFoundException(normalized)));
        }
        return resolved;
    }

    static RoleView toView(Role role) {
        return new RoleView(
                role.id(),
                role.code(),
                role.name(),
                role.isSystem(),
                role.defaultPermissionCodes());
    }
}
