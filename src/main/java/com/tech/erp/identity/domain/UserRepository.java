package com.tech.erp.identity.domain;

import java.util.Optional;
import java.util.UUID;

/**
 * Persistence port for the {@link User} aggregate (ARCHITECTURE.md section 3).
 * Implemented by a JPA adapter in {@code infrastructure}.
 */
public interface UserRepository {

    Optional<User> findById(UUID id);

    boolean existsByEmailValue(String email);

    User save(User user);
}
