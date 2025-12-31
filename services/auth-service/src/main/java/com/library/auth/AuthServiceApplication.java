package com.library.auth;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * Auth Service Application
 * 
 * Microservice responsible for authentication and authorization operations.
 * 
 * Features:
 * - User authentication (login/logout)
 * - JWT token generation and validation
 * - Password management
 * - OAuth2 integration
 * 
 * Architecture: DDD + Clean Architecture
 */
@SpringBootApplication
@EnableDiscoveryClient
public class AuthServiceApplication {
    
    public static void main(String[] args) {
        SpringApplication.run(AuthServiceApplication.class, args);
    }
}
