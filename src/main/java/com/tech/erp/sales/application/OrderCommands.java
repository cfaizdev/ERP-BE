package com.tech.erp.sales.application;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;
import java.util.UUID;

/** Inbound commands for the order use cases (ARCHITECTURE.md section 9.4). */
public final class OrderCommands {

    private OrderCommands() {
    }

    public record CreateOrder(
            @NotNull UUID customerId,
            @NotNull UUID companyId,
            @NotNull UUID branchId) {
    }

    public record AddLine(
            @NotNull UUID productId,
            @Positive int quantity,
            @NotNull @Positive BigDecimal unitPrice,
            @NotNull @Size(min = 3, max = 3) String currency) {
    }
}
