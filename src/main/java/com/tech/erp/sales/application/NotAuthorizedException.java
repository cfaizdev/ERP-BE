package com.tech.erp.sales.application;

import java.util.UUID;

/** Raised when the acting user lacks the permission a use case requires. */
public class NotAuthorizedException extends RuntimeException {

    public NotAuthorizedException(UUID userId, String permission) {
        super("user " + userId + " lacks permission " + permission);
    }
}
