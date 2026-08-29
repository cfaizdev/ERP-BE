package com.tech.erp.identity.application;

/** Raised when registering a user whose email is already taken. */
public class EmailAlreadyUsedException extends RuntimeException {

    public EmailAlreadyUsedException(String email) {
        super("email already in use: " + email);
    }
}
