package com.tech.erp.identity.domain.entities;

/**
 * The verb half of a {@link Permission}. Together with the module it forms the
 * unique {@code (module, action)} pair - one permission per verb per module.
 */
public enum PermissionAction {
    CREATE,
    DELETE,
    APPROVE,
    DOWNLOAD,
    VIEW,
    EDIT,
    CANCEL
}
