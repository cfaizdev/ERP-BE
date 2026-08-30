package com.tech.erp.shared.domain;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.Version;
import java.time.Instant;
import java.util.UUID;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

/**
 * The audit and tenancy columns every persistent entity carries.
 *
 * <p>Not an entity and not a table: {@code @MappedSuperclass} is compile-time
 * inheritance, so Hibernate flattens these six columns into each subclass's own
 * table. {@code identity.users} simply gains {@code company_id}, {@code created_at},
 * {@code created_by}, {@code updated_at}, {@code updated_by} and {@code version} -
 * no join, no second row, no history table. What this records is current state:
 * who last touched the row and when, with the previous value overwritten.
 *
 * <p>{@code company_id} is written now and filtered on later. Multi-tenancy is not
 * live (ARCHITECTURE.md section 10) - the column exists so that switching it on is
 * a read-side infrastructure change rather than a migration of every table.
 *
 * <p>{@code created_by} / {@code updated_by} are nullable because nothing is
 * authenticated yet; see {@code UnauthenticatedContext}. The timestamps and
 * {@code version} need no principal and are populated from day one.
 */
@MappedSuperclass
@EntityListeners({AuditingEntityListener.class, CompanyStampingListener.class})
public abstract class AuditableEntity {

    @Column(name = "company_id")
    private UUID companyId;

    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    @CreatedBy
    @Column(name = "created_by", updatable = false)
    private Long createdBy;

    @LastModifiedDate
    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

    @LastModifiedBy
    @Column(name = "updated_by")
    private Long updatedBy;

    @Version
    @Column(name = "version", nullable = false)
    private long version;

    /**
     * Called by an aggregate that already knows which company it belongs to - a
     * {@code User} is registered into one, and its grant rows inherit it. An
     * explicit value always beats whatever the listener would have resolved.
     */
    protected void assignCompany(UUID companyId) {
        this.companyId = companyId;
    }

    /** Only {@link CompanyStampingListener} calls this, and only on insert. */
    void stampCompanyIfAbsent(UUID resolved) {
        if (this.companyId == null) {
            this.companyId = resolved;
        }
    }

    public UUID companyId() {
        return companyId;
    }

    public Instant createdAt() {
        return createdAt;
    }

    public Long createdBy() {
        return createdBy;
    }

    public Instant updatedAt() {
        return updatedAt;
    }

    public Long updatedBy() {
        return updatedBy;
    }

    public long version() {
        return version;
    }
}
