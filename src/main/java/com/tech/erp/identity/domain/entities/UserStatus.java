package com.tech.erp.identity.domain.entities;

/** Lifecycle state of a {@link User}. Only {@code ACTIVE} users may authenticate. */
public enum UserStatus {
    ACTIVE,
    DISABLED,
    LOCKED
}
