/**
 * Sales bounded context (ARCHITECTURE.md section 9) - the core domain.
 *
 * <p>Owns orders, the order lifecycle and pricing at time of sale. Depends on
 * {@code identity.api} for a synchronous permission check when placing an order;
 * everything else is referenced by id or reacts to domain events.
 */
@org.springframework.modulith.ApplicationModule(displayName = "Sales")
package com.tech.erp.sales;
