package com.library.borrowing;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * Borrowing Service Application
 * 
 * Microservice responsible for managing book borrowing and return operations.
 * 
 * Features:
 * - Borrow book transactions
 * - Return book processing
 * - Borrowing history tracking
 * - Overdue detection and fine calculation
 * - Renewal operations
 * 
 * Architecture: DDD + Clean Architecture
 */
@SpringBootApplication
@EnableDiscoveryClient
@EnableJpaAuditing
@EnableCaching
@EnableScheduling
public class BorrowingServiceApplication {
    
    public static void main(String[] args) {
        SpringApplication.run(BorrowingServiceApplication.class, args);
    }
}
