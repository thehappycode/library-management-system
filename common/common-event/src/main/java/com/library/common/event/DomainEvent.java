package com.library.common.event;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Base interface for all domain events
 * Domain events represent something that happened in the domain
 */
public interface DomainEvent {
    
    /**
     * Get unique event ID
     */
    String getEventId();
    
    /**
     * Get event type/name
     */
    String getEventType();
    
    /**
     * Get timestamp when event occurred
     */
    LocalDateTime getOccurredOn();
    
    /**
     * Get aggregate ID related to this event
     */
    String getAggregateId();
    
    /**
     * Get event version for schema evolution
     */
    default int getVersion() {
        return 1;
    }
    
    /**
     * Generate a unique event ID
     */
    static String generateEventId() {
        return UUID.randomUUID().toString();
    }
}
