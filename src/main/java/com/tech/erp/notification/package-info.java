/**
 * Notification bounded context.
 *
 * <p>Reacts to events published by other modules and turns them into messages to
 * people - email today, anything else later. It listens only to
 * {@code api.events} records and never calls another module back, so nothing here
 * can slow down or fail the use case that triggered it.
 */
@org.springframework.modulith.ApplicationModule(displayName = "Notification")
package com.tech.erp.notification;
