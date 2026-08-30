package com.tech.erp.identity.infrastructure.web;

import com.tech.erp.identity.api.dto.PermissionView;
import com.tech.erp.identity.application.PermissionCatalogService;
import com.tech.erp.identity.application.PermissionCommands.CreatePermission;
import jakarta.validation.Valid;
import java.net.URI;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/** HTTP entry point for the permission catalog. */
@RestController
@RequestMapping("/api/permissions")
class PermissionController {

    private final PermissionCatalogService catalog;

    PermissionController(PermissionCatalogService catalog) {
        this.catalog = catalog;
    }

    @GetMapping
    List<PermissionView> all() {
        return catalog.findAll();
    }

    @PostMapping
    ResponseEntity<PermissionView> create(@Valid @RequestBody CreatePermission command) {
        Long id = catalog.create(command);
        return catalog.findById(id)
                .map(view -> ResponseEntity.created(URI.create("/api/permissions/" + id)).body(view))
                .orElseGet(() -> ResponseEntity.noContent().build());
    }

    @GetMapping("/{id}")
    ResponseEntity<PermissionView> byId(@PathVariable Long id) {
        return catalog.findById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
}
