package com.tech.erp.shared.domain;

import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.Transient;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import org.springframework.data.domain.AfterDomainEventPublication;
import org.springframework.data.domain.DomainEvents;

/**
 * An {@link AuditableEntity} that is also an aggregate root, and so can publish
 * domain events on save.
 *
 * <p>This exists because Java has single inheritance and Spring Data's
 * {@code AbstractAggregateRoot} occupies the one superclass slot. Rather than give
 * up auditing on every root, the ten lines of event plumbing are restated here on
 * top of the audit base - the contract with Spring Data is the two annotations
 * below, not the class it happens to be declared on.
 *
 * <p>Extend this only for roots that actually publish events; everything else
 * extends {@link AuditableEntity} directly and stays free of the event list.
 */
@MappedSuperclass
public abstract class AuditableAggregateRoot<A extends AuditableAggregateRoot<A>> extends AuditableEntity {

    @Transient
    private final transient List<Object> domainEvents = new ArrayList<>();

    /** Queues an event for publication the next time the aggregate is saved. */
    protected <T> T registerEvent(T event) {
        domainEvents.add(Objects.requireNonNull(event, "event must not be null"));
        return event;
    }

    @DomainEvents
    protected Collection<Object> domainEvents() {
        return Collections.unmodifiableList(domainEvents);
    }

    @AfterDomainEventPublication
    protected void clearDomainEvents() {
        domainEvents.clear();
    }
}
