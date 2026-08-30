package com.tech.erp.identity.infrastructure.web;

import com.tech.erp.identity.api.dto.RoleView;
import com.tech.erp.identity.application.RoleCommands.CreateRole;
import com.tech.erp.identity.application.RoleCommands.SetDefaults;
import com.tech.erp.identity.application.RoleService;
import jakarta.validation.Valid;
import java.net.URI;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/** HTTP entry point for role administration. */
@RestController
@RequestMapping("/api/roles")
class RoleController {

    private final RoleService roles;

    RoleController(RoleService roles) {
        this.roles = roles;
    }

    @GetMapping
    List<RoleView> all() {
        return roles.findAll();
    }

    @PostMapping
    ResponseEntity<RoleView> create(@Valid @RequestBody CreateRole command) {
        Long id = roles.create(command);
        return roles.findById(id)
                .map(view -> ResponseEntity.created(URI.create("/api/roles/" + id)).body(view))
                .orElseGet(() -> ResponseEntity.noContent().build());
    }

    @GetMapping("/{id}")
    ResponseEntity<RoleView> byId(@PathVariable Long id) {
        return roles.findById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    /** Replaces the whole default set. Users already holding the role are not re-seeded. */
    @PutMapping("/{id}/permissions")
    ResponseEntity<RoleView> setDefaults(@PathVariable Long id, @Valid @RequestBody SetDefaults command) {
        roles.setDefaults(id, command);
        return roles.findById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
}
