package com.library.common.event;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.library.common.dto.UserDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Base class for user-related domain events
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type")
@JsonSubTypes({
    @JsonSubTypes.Type(value = UserEvent.UserRegisteredEvent.class, name = "USER_REGISTERED"),
    @JsonSubTypes.Type(value = UserEvent.UserUpdatedEvent.class, name = "USER_UPDATED"),
    @JsonSubTypes.Type(value = UserEvent.UserDeletedEvent.class, name = "USER_DELETED"),
    @JsonSubTypes.Type(value = UserEvent.UserActivatedEvent.class, name = "USER_ACTIVATED"),
    @JsonSubTypes.Type(value = UserEvent.UserDeactivatedEvent.class, name = "USER_DEACTIVATED")
})
public abstract class UserEvent implements DomainEvent {
    
    private String eventId;
    private LocalDateTime occurredOn;
    private String aggregateId;
    
    public UserEvent(String aggregateId) {
        this.eventId = DomainEvent.generateEventId();
        this.occurredOn = LocalDateTime.now();
        this.aggregateId = aggregateId;
    }
    
    @Override
    public String getEventId() {
        return eventId;
    }
    
    @Override
    public LocalDateTime getOccurredOn() {
        return occurredOn;
    }
    
    @Override
    public String getAggregateId() {
        return aggregateId;
    }
    
    /**
     * User Registered Event
     */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UserRegisteredEvent extends UserEvent {
        private UserDTO user;
        
        public UserRegisteredEvent(String aggregateId, UserDTO user) {
            super(aggregateId);
            this.user = user;
        }
        
        @Override
        public String getEventType() {
            return "USER_REGISTERED";
        }
    }
    
    /**
     * User Updated Event
     */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UserUpdatedEvent extends UserEvent {
        private UserDTO user;
        
        public UserUpdatedEvent(String aggregateId, UserDTO user) {
            super(aggregateId);
            this.user = user;
        }
        
        @Override
        public String getEventType() {
            return "USER_UPDATED";
        }
    }
    
    /**
     * User Deleted Event
     */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UserDeletedEvent extends UserEvent {
        private Long userId;
        
        public UserDeletedEvent(String aggregateId, Long userId) {
            super(aggregateId);
            this.userId = userId;
        }
        
        @Override
        public String getEventType() {
            return "USER_DELETED";
        }
    }
    
    /**
     * User Activated Event
     */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UserActivatedEvent extends UserEvent {
        private Long userId;
        private String username;
        
        public UserActivatedEvent(String aggregateId, Long userId, String username) {
            super(aggregateId);
            this.userId = userId;
            this.username = username;
        }
        
        @Override
        public String getEventType() {
            return "USER_ACTIVATED";
        }
    }
    
    /**
     * User Deactivated Event
     */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UserDeactivatedEvent extends UserEvent {
        private Long userId;
        private String username;
        private String reason;
        
        public UserDeactivatedEvent(String aggregateId, Long userId, String username, String reason) {
            super(aggregateId);
            this.userId = userId;
            this.username = username;
            this.reason = reason;
        }
        
        @Override
        public String getEventType() {
            return "USER_DEACTIVATED";
        }
    }
}
