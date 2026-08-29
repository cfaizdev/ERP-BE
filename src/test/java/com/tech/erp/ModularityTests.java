package com.tech.erp;

import org.junit.jupiter.api.Test;
import org.springframework.modulith.core.ApplicationModules;

/**
 * Enforces the architecture (ARCHITECTURE.md section 11). Fails the build the first
 * time a module imports another module's non-{@code api} types.
 */
class ModularityTests {

    private static final ApplicationModules MODULES = ApplicationModules.of(ErpApplication.class);

    @Test
    void verifiesModuleBoundaries() {
        MODULES.verify();
    }

    @Test
    void printModuleStructure() {
        MODULES.forEach(System.out::println);
    }
}
