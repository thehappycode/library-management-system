package com.library.sagaorchestrator;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * Saga Orchestrator Service Application
 * 
 * Microservice responsible for managing distributed transactions using Saga pattern.
 * 
 * Features:
 * - Orchestrate distributed transactions
 * - Handle compensation logic
 * - Track saga execution state
 * - Retry failed transactions
 * - Event-driven saga coordination
 * 
 * Architecture: DDD + Clean Architecture
 */
@SpringBootApplication
@EnableDiscoveryClient
@EnableScheduling
public class SagaOrchestratorServiceApplication {
    
    public static void main(String[] args) {
        SpringApplication.run(SagaOrchestratorServiceApplication.class, args);
    }
}
