package com.tech.erp.notification.application;

import com.tech.erp.identity.api.events.UserRegistered;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.modulith.events.ApplicationModuleListener;
import org.springframework.stereotype.Component;

/**
 * Sends the welcome email when a principal is registered.
 *
 * <p>{@link ApplicationModuleListener} is {@code @Async} +
 * {@code @Transactional(REQUIRES_NEW)} + {@code @TransactionalEventListener} in one
 * annotation, which is exactly what this needs: it runs only after the
 * registration transaction commits (no email for a signup that rolled back), it
 * runs off the request thread (a slow mail server cannot slow down
 * {@code POST /api/users}), and because {@code spring-modulith-starter-jpa} is on
 * the classpath the delivery is recorded in {@code event_publication} - so an
 * email lost to a crash or an outage can be republished rather than vanishing.
 */
@Component
class WelcomeEmailListener {

    private static final Logger log = LoggerFactory.getLogger(WelcomeEmailListener.class);

    @ApplicationModuleListener
    void on(UserRegistered event) {
        // TODO: hand off to the mail transport once one is wired up. Keep the work
        // inside this method - the transactional and retry behaviour above is what
        // makes a dropped welcome email recoverable.
        log.info("welcome email queued for user {} <{}> registered at {}",
                event.userId(), event.email(), event.registeredAt());
    }
}
