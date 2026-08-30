package com.tech.erp.identity.application;

import com.tech.erp.identity.domain.entities.PermissionAction;

/**
 * Raised when a catalog entry would collide with an existing one - either on the
 * code or on the unique {@code (module, action)} pair.
 */
public class DuplicatePermissionCodeException extends RuntimeException {

    public DuplicatePermissionCodeException(String code) {
        super("permission code already defined: " + code);
    }

    public DuplicatePermissionCodeException(String module, PermissionAction action) {
        super("permission already defined for " + module + "/" + action);
    }
}
