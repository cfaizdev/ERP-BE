package com.tech.erp.identity.application;

import com.tech.erp.identity.domain.User;
import com.tech.erp.identity.domain.UserRepository;
import java.util.UUID;
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
    public UUID register(RegisterUserCommand command) {
        if (users.existsByEmailValue(command.normalizedEmail())) {
            throw new EmailAlreadyUsedException(command.normalizedEmail());
        }
        User user = User.register(
                command.email(),
                passwordEncoder.encode(command.password()),
                command.companyId(),
                command.branchId());
        return users.save(user).id();
    }
}
