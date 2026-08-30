/**
 * Adapters for the Identity module.
 *
 * <p>Implements the ports declared in {@code domain} and exposes the module to
 * the outside world. One subpackage per adapter kind: {@code persistence} for
 * the JPA repositories, {@code web} for the REST entry points, {@code config}
 * for bean wiring.
 *
 * <p>Nothing here is part of the module's published contract. Other modules
 * import {@link com.tech.erp.identity.api} and nothing else; the REST layer in
 * {@code web} serves the outside world only, never another module.
 */
package com.tech.erp.identity.infrastructure;
