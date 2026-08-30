package com.tech.erp.identity.application;

import com.tech.erp.identity.domain.entities.User;
import com.tech.erp.identity.domain.jpa.UserRepository;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Registers new principals (ARCHITECTURE.md section 8.1). Owns the transaction;
 * password hashing and the uniqueness check are enforced here / in the aggregate.
 */
@Service
public class RegisterUserService {

    private final UserRepository users;
    private final PasswordEncoder passwordEncoder;

    RegisterUserService(UserRepository users, PasswordEncoder passwordEncoder) {
        this.users = users;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public Long register(RegisterUserCommand command) {
        if (users.existsByEmailValue(command.normalizedEmail())) {
            throw new EmailAlreadyUsedException(command.normalizedEmail());
        }
        User user = User.register(
                command.email(),
                passwordEncoder.encode(command.password()),
                command.companyId(),
                command.branchId());

        // The id is database-generated, so the event can only be built once the row
        // exists - otherwise every listener (welcome email included) sees a null id.
        User saved = users.save(user);
        saved.markRegistered();
        return users.save(saved).id();
    }
}
