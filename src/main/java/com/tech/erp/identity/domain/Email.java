package com.tech.erp.identity.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import java.util.Objects;
import java.util.regex.Pattern;

/** Email value object - normalized and format-checked on construction. Unique per system. */
@Embeddable
public class Email {

    private static final Pattern FORMAT = Pattern.compile("^[^@\\s]+@[^@\\s]+\\.[^@\\s]+$");

    @Column(name = "email", nullable = false, unique = true)
    private String value;

    protected Email() {
        // for JPA
    }

    Email(String value) {
        Objects.requireNonNull(value, "email must not be null");
        String normalized = value.trim().toLowerCase();
        if (!FORMAT.matcher(normalized).matches()) {
            throw new IllegalArgumentException("invalid email: " + value);
        }
        this.value = normalized;
    }

    String value() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        return o instanceof Email other && Objects.equals(value, other.value);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(value);
    }

    @Override
    public String toString() {
        return value;
    }
}
