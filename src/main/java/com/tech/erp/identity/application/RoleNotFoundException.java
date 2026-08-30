package com.tech.erp.identity.application;

/** Raised when a role code or id does not exist. */
public class RoleNotFoundException extends RuntimeException {

    public RoleNotFoundException(String code) {
        super("no such role: " + code);
    }

    public RoleNotFoundException(Long id) {
        super("no such role: " + id);
    }
}
