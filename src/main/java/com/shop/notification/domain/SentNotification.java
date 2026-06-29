package com.shop.notification.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "sent_notifications")
public class SentNotification {

    @Id
    @Column(name = "event_id")
    private String eventId;

    @Column(nullable = false)
    private String channel;

    @Column(name = "event_type", nullable = false)
    private String eventType;

    @Column(name = "order_id", nullable = false)
    private String orderId;

    @Column(nullable = false)
    private String message;

    protected SentNotification() {
    }

    public SentNotification(String eventId, String channel, String eventType, String orderId, String message) {
        this.eventId = eventId;
        this.channel = channel;
        this.eventType = eventType;
        this.orderId = orderId;
        this.message = message;
    }

    public String getEventId() {
        return eventId;
    }

    public String getOrderId() {
        return orderId;
    }

    public String getMessage() {
        return message;
    }
}
