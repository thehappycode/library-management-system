package com.library.common.event;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * Base class for notification-related domain events
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type")
@JsonSubTypes({
    @JsonSubTypes.Type(value = NotificationEvent.EmailNotificationEvent.class, name = "EMAIL_NOTIFICATION"),
    @JsonSubTypes.Type(value = NotificationEvent.SmsNotificationEvent.class, name = "SMS_NOTIFICATION"),
    @JsonSubTypes.Type(value = NotificationEvent.PushNotificationEvent.class, name = "PUSH_NOTIFICATION")
})
public abstract class NotificationEvent implements DomainEvent {
    
    private String eventId;
    private LocalDateTime occurredOn;
    private String aggregateId;
    
    public NotificationEvent(String aggregateId) {
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
     * Email Notification Event
     */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class EmailNotificationEvent extends NotificationEvent {
        private String to;
        private String subject;
        private String body;
        private Map<String, String> templateData;
        private String templateName;
        
        public EmailNotificationEvent(String aggregateId, String to, String subject, 
                                     String body, Map<String, String> templateData, String templateName) {
            super(aggregateId);
            this.to = to;
            this.subject = subject;
            this.body = body;
            this.templateData = templateData;
            this.templateName = templateName;
        }
        
        @Override
        public String getEventType() {
            return "EMAIL_NOTIFICATION";
        }
    }
    
    /**
     * SMS Notification Event
     */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SmsNotificationEvent extends NotificationEvent {
        private String phoneNumber;
        private String message;
        
        public SmsNotificationEvent(String aggregateId, String phoneNumber, String message) {
            super(aggregateId);
            this.phoneNumber = phoneNumber;
            this.message = message;
        }
        
        @Override
        public String getEventType() {
            return "SMS_NOTIFICATION";
        }
    }
    
    /**
     * Push Notification Event
     */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PushNotificationEvent extends NotificationEvent {
        private Long userId;
        private String title;
        private String body;
        private Map<String, Object> data;
        
        public PushNotificationEvent(String aggregateId, Long userId, String title, 
                                    String body, Map<String, Object> data) {
            super(aggregateId);
            this.userId = userId;
            this.title = title;
            this.body = body;
            this.data = data;
        }
        
        @Override
        public String getEventType() {
            return "PUSH_NOTIFICATION";
        }
    }
}
