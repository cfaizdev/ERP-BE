package com.tech.erp.sales.domain;

import java.util.UUID;

/** An order with no lines cannot be placed (ARCHITECTURE.md section 9.1). */
public class EmptyOrderException extends RuntimeException {

    public EmptyOrderException(UUID orderId) {
        super("order " + orderId + " has no lines and cannot be placed");
    }
}
