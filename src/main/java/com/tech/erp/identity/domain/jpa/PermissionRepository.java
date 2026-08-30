package com.tech.erp.identity.domain.jpa;

import com.tech.erp.identity.domain.entities.Permission;
import com.tech.erp.identity.domain.entities.PermissionAction;

import java.util.List;
import java.util.Optional;

/** Persistence port for the {@link Permission} catalog. */
public interface PermissionRepository {

    Optional<Permission> findById(Long id);

    Optional<Permission> findByCode(String code);

    List<Permission> findAll();

    boolean existsByCode(String code);

    boolean existsByModuleAndAction(String module, PermissionAction action);

    Permission save(Permission permission);
}
