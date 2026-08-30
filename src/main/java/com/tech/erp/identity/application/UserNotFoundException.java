package com.tech.erp.identity.application;

/** Raised when a use case is handed a user id that does not exist. */
public class UserNotFoundException extends RuntimeException {

    public UserNotFoundException(Long userId) {
        super("no such user: " + userId);
    }
}
