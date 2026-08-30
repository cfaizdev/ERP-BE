package com.tech.erp.identity.domain.entities;

/**
 * Provenance of a {@link UserPermission} row - the tiebreaker between the two
 * access mechanisms.
 *
 * <p>{@code ROLE_DEFAULT} rows were seeded by assigning a {@link Role} and may be
 * re-seeded freely. {@code MANUAL} rows are a deliberate per-user decision and are
 * never overwritten by a role assignment, which is what lets an administrator deny
 * one permission to a user who otherwise keeps the whole role.
 */
public enum GrantSource {
    ROLE_DEFAULT,
    MANUAL
}
