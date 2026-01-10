package com.library.bookservice.infrastructure.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * Database configuration
 */
@Configuration
@EnableJpaRepositories(basePackages = "com.library.bookservice.infrastructure.persistence. repository")
@EnableTransactionManagement
public class DatabaseConfig {

    // Additional database configurations can go here
    // For now, application.yml handles most settings
}