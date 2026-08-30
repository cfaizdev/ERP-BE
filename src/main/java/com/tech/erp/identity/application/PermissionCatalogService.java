package com.tech.erp.identity.application;

import com.tech.erp.identity.api.dto.PermissionView;
import com.tech.erp.identity.application.PermissionCommands.CreatePermission;
import com.tech.erp.identity.domain.entities.Permission;
import com.tech.erp.identity.domain.jpa.PermissionRepository;

import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Maintains the catalog of grantable permissions. Adding a capability to the
 * system is a call to {@link #create} - never a schema change.
 */
@Service
@Transactional
public class PermissionCatalogService {

    private final PermissionRepository permissions;

    PermissionCatalogService(PermissionRepository permissions) {
        this.permissions = permissions;
    }

    public Long create(CreatePermission command) {
        String code = Permission.normalizeCode(command.code(), "permission code");
        String module = Permission.normalizeCode(command.module(), "module");

        // Checked up front so the caller gets a 409 with a readable message rather
        // than a constraint violation surfacing as a 500.
        if (permissions.existsByCode(code)) {
            throw new DuplicatePermissionCodeException(code);
        }
        if (permissions.existsByModuleAndAction(module, command.action())) {
            throw new DuplicatePermissionCodeException(module, command.action());
        }
        Permission permission = Permission.define(code, module, command.action(), command.description());
        return permissions.save(permission).id();
    }

    @Transactional(readOnly = true)
    public List<PermissionView> findAll() {
        return permissions.findAll().stream().map(PermissionCatalogService::toView).toList();
    }

    @Transactional(readOnly = true)
    public Optional<PermissionView> findById(Long id) {
        return permissions.findById(id).map(PermissionCatalogService::toView);
    }

    static PermissionView toView(Permission permission) {
        return new PermissionView(
                permission.id(),
                permission.code(),
                permission.module(),
                permission.action().name(),
                permission.description());
    }
}
