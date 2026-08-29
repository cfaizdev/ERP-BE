package com.tech.erp.identity.infrastructure;

import com.tech.erp.identity.domain.User;
import com.tech.erp.identity.domain.UserRepository;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * JPA adapter for {@link UserRepository}. Spring Data supplies the implementation;
 * {@code existsByEmailValue} is derived from the {@code email.value} property.
 */
interface UserJpaRepository extends UserRepository, JpaRepository<User, UUID> {
}
