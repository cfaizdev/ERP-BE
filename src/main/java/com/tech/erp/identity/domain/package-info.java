/**
 * The Identity domain model and its persistence ports.
 *
 * <p>Holds the aggregate roots, their value objects, and the repository
 * interfaces the {@code application} layer depends on. This layer knows nothing
 * about HTTP, messaging, or any other module - {@code infrastructure} adapts to
 * it, never the reverse.
 *
 * <p>Split in two: {@code entities} holds the model - the {@code User} aggregate
 * with its grant rows, the {@code Permission} catalog and the {@code Role}
 * templates - and {@code jpa} holds the ports those aggregates are loaded and
 * stored through. The adapters implementing the ports live one layer out, in
 * {@code infrastructure.persistence}.
 *
 * <p>One deliberate exception to "the domain is framework-free": JPA mapping
 * annotations live on the model itself rather than on a parallel set of
 * persistence entities. Identity is small enough that a second model plus
 * mappers would cost more than it buys.
 *
 * <p>Types that never leave these packages are package-private.
 */
package com.tech.erp.identity.domain;
