package com.tech.erp.identity.domain.jpa;

import com.tech.erp.identity.domain.entities.Role;

import java.util.List;
import java.util.Optional;

/** Persistence port for the {@link Role} templates. */
public interface RoleRepository {

    Optional<Role> findById(Long id);

    /** Loads the role with its default permissions fetched - needed to seed a user's grants. */
    Optional<Role> findWithDefaultsById(Long id);

    Optional<Role> findByCode(String code);

    List<Role> findAll();

    boolean existsByCode(String code);

    Role save(Role role);
}
