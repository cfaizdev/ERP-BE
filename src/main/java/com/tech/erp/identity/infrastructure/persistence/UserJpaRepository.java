package com.tech.erp.identity.infrastructure.persistence;

import com.tech.erp.identity.domain.entities.User;
import com.tech.erp.identity.domain.jpa.UserRepository;

import java.util.Optional;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * JPA adapter for {@link UserRepository}. Spring Data supplies the implementation;
 * {@code existsByEmailValue} is derived from the {@code email.value} property, and
 * the text between {@code find} and {@code By} in {@code findWithAccessById} is
 * just a label - the entity graph below is what does the fetching.
 */
interface UserJpaRepository extends UserRepository, JpaRepository<User, Long> {

    @EntityGraph(attributePaths = {"permissions", "permissions.permission"})
    @Override
    Optional<User> findWithAccessById(Long id);
}
