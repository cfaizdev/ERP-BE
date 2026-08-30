package com.tech.erp.identity.infrastructure.web;

import com.tech.erp.identity.api.dto.UserPermissionView;
import com.tech.erp.identity.application.UserAccessCommands.AssignRole;
import com.tech.erp.identity.application.UserAccessCommands.Decide;
import com.tech.erp.identity.application.UserAccessService;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * HTTP entry point for one user's access - both the RBAC path (assign a role) and
 * the customization path (decide a single permission).
 */
@RestController
@RequestMapping("/api/users/{userId}")
class UserAccessController {

    private final UserAccessService access;

    UserAccessController(UserAccessService access) {
        this.access = access;
    }

    /** Seeds the role's defaults. Existing manual decisions are preserved. */
    @PostMapping("/roles")
    ResponseEntity<List<UserPermissionView>> assignRole(
            @PathVariable Long userId, @Valid @RequestBody AssignRole command) {
        access.assignRole(userId, command);
        return ResponseEntity.ok(access.grantsOf(userId));
    }

    /** Every decision on record, denies included. */
    @GetMapping("/permissions")
    List<UserPermissionView> permissions(@PathVariable Long userId) {
        return access.grantsOf(userId);
    }

    /** Grant ({@code granted: true}) or explicitly deny ({@code granted: false}). */
    @PutMapping("/permissions/{code}")
    ResponseEntity<List<UserPermissionView>> decide(
            @PathVariable Long userId, @PathVariable String code, @Valid @RequestBody Decide command) {
        access.decide(userId, code, command);
        return ResponseEntity.ok(access.grantsOf(userId));
    }

    /** Removes the decision outright, so a later role assignment can seed it again. */
    @DeleteMapping("/permissions/{code}")
    ResponseEntity<Void> revoke(@PathVariable Long userId, @PathVariable String code) {
        access.revoke(userId, code);
        return ResponseEntity.noContent().build();
    }
}
