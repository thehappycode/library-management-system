package com.library.discovery;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

/**
 * Service Discovery Application
 * 
 * Eureka Server for service registration and discovery.
 * All microservices register with this server and discover other services.
 * 
 * Dashboard: http://localhost:8761
 */
@SpringBootApplication
@EnableEurekaServer
public class ServiceDiscoveryApplication {
    
    public static void main(String[] args) {
        SpringApplication.run(ServiceDiscoveryApplication.class, args);
    }
}
