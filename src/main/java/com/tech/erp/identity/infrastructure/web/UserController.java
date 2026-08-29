package com.tech.erp.identity.infrastructure.web;

import com.tech.erp.identity.api.IdentityApi;
import com.tech.erp.identity.api.dto.UserView;
import com.tech.erp.identity.application.RegisterUserCommand;
import com.tech.erp.identity.application.RegisterUserService;
import jakarta.validation.Valid;
import java.net.URI;
import java.util.UUID;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/** HTTP entry point for Identity - the outside world only (ARCHITECTURE.md section 4). */
@RestController
@RequestMapping("/api/users")
class UserController {

    private final RegisterUserService registerUser;
    private final IdentityApi identity;

    UserController(RegisterUserService registerUser, IdentityApi identity) {
        this.registerUser = registerUser;
        this.identity = identity;
    }

    @PostMapping
    ResponseEntity<UserView> register(@Valid @RequestBody RegisterUserCommand command) {
        UUID id = registerUser.register(command);
        return identity.findUser(id)
                .map(view -> ResponseEntity.created(URI.create("/api/users/" + id)).body(view))
                .orElseGet(() -> ResponseEntity.noContent().build());
    }

    @GetMapping("/{id}")
    ResponseEntity<UserView> byId(@PathVariable UUID id) {
        return identity.findUser(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
}
