package com.library.book;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

/**
 * Book Service Application
 * 
 * Microservice responsible for managing book catalog, inventory, and search operations.
 * 
 * Features:
 * - Book CRUD operations
 * - Category management
 * - Inventory tracking
 * - Search and filtering
 * 
 * Architecture: DDD + Clean Architecture
 * - Domain: Business logic and entities
 * - Application: Use cases and ports
 * - Infrastructure: Technical implementations
 * - Presentation: REST API endpoints
 */
@SpringBootApplication
@EnableDiscoveryClient
@EnableJpaAuditing
@EnableCaching
public class BookServiceApplication {
    
    public static void main(String[] args) {
        SpringApplication.run(BookServiceApplication.class, args);
    }
}
