package com.tech.erp.identity.application;

/** Raised when a role code is already taken. */
public class DuplicateRoleCodeException extends RuntimeException {

    public DuplicateRoleCodeException(String code) {
        super("role code already defined: " + code);
    }
}
