package com.tech.erp.identity.infrastructure.persistence;

import com.tech.erp.identity.domain.entities.Role;
import com.tech.erp.identity.domain.jpa.RoleRepository;

import java.util.Optional;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

/** JPA adapter for {@link RoleRepository}. */
interface RoleJpaRepository extends RoleRepository, JpaRepository<Role, Long> {

    @EntityGraph(attributePaths = "defaultPermissions")
    @Override
    Optional<Role> findWithDefaultsById(Long id);
}
