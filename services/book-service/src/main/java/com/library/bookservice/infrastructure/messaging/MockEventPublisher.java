package com.library.bookservice.infrastructure.messaging;

import org.springframework.stereotype.Component;

import com.library.bookservice.application.port.output.EventPublisherPort;
import com.library.bookservice.domain.event.BookEvent;

import lombok.extern.slf4j.Slf4j;

/**
 * Mock implementation of EventPublisherPort
 * Just logs events for now
 * TODO: Replace with real Kafka implementation later
 */
@Component
@Slf4j
public class MockEventPublisher implements EventPublisherPort {

    @Override
    public void publish(BookEvent event) {
        log.info("📨 [MOCK] Publishing event synchronously: {} - {} - BookID: {}",
                event.getEventType(),
                event.getEventId(),
                event.getBookId());

        // In real implementation, this would send to Kafka
        log.debug("Event details: ISBN={}, Title={}", event.getIsbn(), event.getTitle());
    }

    @Override
    public void publishAsync(BookEvent event) {
        log.info("📨 [MOCK] Publishing event asynchronously: {} - {} - BookID: {}",
                event.getEventType(),
                event.getEventId(),
                event.getBookId());

        // In real implementation, this would send to Kafka asynchronously
        log.debug("Event details: ISBN={}, Title={}", event.getIsbn(), event.getTitle());
    }
}