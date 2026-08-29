package com.tech.erp.identity.application;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.UUID;

/** Input to {@link RegisterUserService#register}. */
public record RegisterUserCommand(
        @Email @NotBlank String email,
        @NotBlank @Size(min = 8, max = 100) String password,
        @NotNull UUID companyId,
        @NotNull UUID branchId) {

    public String normalizedEmail() {
        return email == null ? null : email.trim().toLowerCase();
    }
}
