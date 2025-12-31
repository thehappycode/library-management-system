package com.library.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * API Gateway Application
 * 
 * Central entry point for all client requests to microservices.
 * 
 * Features:
 * - Routing and load balancing
 * - Authentication and authorization
 * - Rate limiting
 * - Request/response transformation
 * - Circuit breaker integration
 * - CORS configuration
 */
@SpringBootApplication
@EnableDiscoveryClient
public class ApiGatewayApplication {
    
    public static void main(String[] args) {
        SpringApplication.run(ApiGatewayApplication.class, args);
    }
}
