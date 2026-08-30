package com.tech.erp.identity.infrastructure.persistence;

import com.tech.erp.identity.domain.entities.Permission;
import com.tech.erp.identity.domain.jpa.PermissionRepository;

import org.springframework.data.jpa.repository.JpaRepository;

/** JPA adapter for {@link PermissionRepository} - every method is derived. */
interface PermissionJpaRepository extends PermissionRepository, JpaRepository<Permission, Long> {
}
