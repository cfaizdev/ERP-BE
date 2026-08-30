package com.tech.erp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

/**
 * Async is enabled for {@code @ApplicationModuleListener}, which is
 * {@code @Async} underneath - without it event listeners would run on the caller's
 * thread and a slow one would show up as request latency.
 */
@EnableAsync
@SpringBootApplication
public class ErpApplication {

	public static void main(String[] args) {
		SpringApplication.run(ErpApplication.class, args);
	}

}
