package com.tech.erp.sales.application;

import java.util.UUID;

/** Raised when an order id does not resolve. */
public class OrderNotFoundException extends RuntimeException {

    public OrderNotFoundException(UUID orderId) {
        super("order not found: " + orderId);
    }
}
