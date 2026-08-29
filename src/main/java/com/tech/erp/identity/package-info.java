/**
 * Identity &amp; Access bounded context (ARCHITECTURE.md section 8.1).
 *
 * <p>Owns users, credentials, permissions and authentication. Other modules
 * consult this context only through {@link com.tech.erp.identity.api} - never by
 * importing {@code domain}, {@code application} or {@code infrastructure} types.
 */
@org.springframework.modulith.ApplicationModule(displayName = "Identity & Access")
package com.tech.erp.identity;
