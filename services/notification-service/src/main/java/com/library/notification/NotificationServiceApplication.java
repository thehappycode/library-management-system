package com.library.notification;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * Notification Service Application
 * 
 * Microservice responsible for sending notifications to users.
 * 
 * Features:
 * - Email notifications
 * - SMS notifications (optional)
 * - Push notifications
 * - Notification templates
 * - Event-driven notification processing
 * 
 * Architecture: DDD + Clean Architecture
 */
@SpringBootApplication
@EnableDiscoveryClient
@EnableAsync
@EnableScheduling
public class NotificationServiceApplication {
    
    public static void main(String[] args) {
        SpringApplication.run(NotificationServiceApplication.class, args);
    }
}
