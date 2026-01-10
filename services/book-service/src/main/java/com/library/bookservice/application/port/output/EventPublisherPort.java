package com.library.bookservice.application.port.output;

import com.library.bookservice.domain.event.BookEvent;

/**
 * Output Port for Event Publishing
 * Infrastructure layer will implement this (Kafka)
 */
public interface EventPublisherPort {

    /**
     * Publish event synchronously
     * 
     * @param event
     */
    void publish(BookEvent event);

    /**
     * Publish event asynchronously
     * 
     * @param event
     */
    void publishAsync(BookEvent event);
}