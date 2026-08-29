package com.tech.erp.identity.application;

import com.tech.erp.identity.api.IdentityApi;
import com.tech.erp.identity.api.dto.UserView;
import com.tech.erp.identity.domain.User;
import com.tech.erp.identity.domain.UserRepository;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/** In-process implementation of the published {@link IdentityApi} (section 8.1). */
@Service
@Transactional(readOnly = true)
public class IdentityQueryService implements IdentityApi {

    private final UserRepository users;

    IdentityQueryService(UserRepository users) {
        this.users = users;
    }

    @Override
    public Optional<UserView> findUser(UUID userId) {
        return users.findById(userId).map(IdentityQueryService::toView);
    }

    @Override
    public boolean hasPermission(UUID userId, String permission) {
        return users.findById(userId).map(user -> user.hasPermission(permission)).orElse(false);
    }

    @Override
    public Set<String> permissionsOf(UUID userId) {
        return users.findById(userId).map(User::permissions).orElseGet(Set::of);
    }

    private static UserView toView(User user) {
        return new UserView(
                user.id(),
                user.emailValue(),
                user.statusName(),
                user.permissions(),
                user.companyId(),
                user.branchId());
    }
}
