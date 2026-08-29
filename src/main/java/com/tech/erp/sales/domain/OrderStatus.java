package com.tech.erp.sales.domain;

/** Order lifecycle (ARCHITECTURE.md section 9.1): DRAFT -> PLACED -> CONFIRMED -> FULFILLED / CANCELLED. */
enum OrderStatus {
    DRAFT,
    PLACED,
    CONFIRMED,
    FULFILLED,
    CANCELLED
}
