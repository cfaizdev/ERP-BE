package com.tech.erp.identity.domain.jpa;

import com.tech.erp.identity.domain.entities.User;

import java.util.Optional;

/**
 * Persistence port for the {@link User} aggregate (ARCHITECTURE.md section 3).
 * Implemented by a JPA adapter in {@code infrastructure.persistence}.
 */
public interface UserRepository {

    Optional<User> findById(Long id);

    /**
     * Loads the user with the grant rows and their permissions already fetched.
     * Use this for anything that reads or changes access - {@code open-in-view} is
     * off, so the lazy collection cannot be walked after the transaction ends.
     */
    Optional<User> findWithAccessById(Long id);

    boolean existsByEmailValue(String email);

    User save(User user);
}
