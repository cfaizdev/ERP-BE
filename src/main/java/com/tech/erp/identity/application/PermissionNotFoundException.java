package com.tech.erp.identity.application;

/** Raised when a permission code is not in the catalog. */
public class PermissionNotFoundException extends RuntimeException {

    public PermissionNotFoundException(String code) {
        super("no such permission: " + code);
    }

    public PermissionNotFoundException(Long id) {
        super("no such permission: " + id);
    }
}
