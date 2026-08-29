package com.tech.erp.identity.domain;

/** Lifecycle state of a {@link User}. Only {@code ACTIVE} users may authenticate. */
enum UserStatus {
    ACTIVE,
    DISABLED,
    LOCKED
}
