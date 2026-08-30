/**
 * Cross-cutting building blocks every bounded context extends: the audit/tenancy
 * base entities and the current-actor seam (ARCHITECTURE.md sections 5 and 10).
 *
 * <p>Deliberately {@code OPEN}. A bounded context publishes a narrow {@code api}
 * package and hides the rest; this module is the opposite - its sub-packages
 * <em>are</em> the contract, because other modules extend {@code AuditableEntity}
 * directly rather than calling it.
 *
 * <p>Depends on no other module, and must stay that way: anything that needs to
 * know about users, companies or orders belongs in the context that owns them.
 */
@org.springframework.modulith.ApplicationModule(
        displayName = "Shared Kernel",
        type = org.springframework.modulith.ApplicationModule.Type.OPEN)
package com.tech.erp.shared;
