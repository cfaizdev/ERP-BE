package com.tech.erp.sales.domain;

import java.util.UUID;

/** A CANCELLED or FULFILLED order cannot be modified (ARCHITECTURE.md section 9.1). */
public class OrderNotModifiableException extends RuntimeException {

    public OrderNotModifiableException(UUID orderId, String status) {
        super("order " + orderId + " is " + status + " and cannot be modified");
    }
}
