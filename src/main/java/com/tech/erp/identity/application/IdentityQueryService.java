package com.tech.erp.identity.application;

import com.tech.erp.identity.api.IdentityApi;
import com.tech.erp.identity.api.dto.UserView;
import com.tech.erp.identity.domain.entities.User;
import com.tech.erp.identity.domain.jpa.UserRepository;

import java.util.Optional;
import java.util.Set;
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
    public Optional<UserView> findUser(Long userId) {
        return users.findWithAccessById(userId).map(IdentityQueryService::toView);
    }

    @Override
    public boolean hasPermission(Long userId, String permissionCode) {
        return users.findWithAccessById(userId)
                .map(user -> user.hasPermission(permissionCode))
                .orElse(false);
    }

    @Override
    public Set<String> permissionsOf(Long userId) {
        return users.findWithAccessById(userId).map(User::permissions).orElseGet(Set::of);
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
