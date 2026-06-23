package com.shop.notification.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.shop.notification.domain.SentNotification;
import com.shop.notification.repo.SentNotificationRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

/**
 * Leaf consumer: reacts to terminal order events and sends an (idempotent)
 * customer notification. Publishes nothing.
 */
@Service
public class NotificationService {

    private static final Logger log = LoggerFactory.getLogger(NotificationService.class);
    private static final ObjectMapper MAPPER = new ObjectMapper();

    private static final Map<String, String> MESSAGES = Map.of(
            "OrderConfirmed", "purchase success",
            "OrderCancelled", "payment failed",
            "OrderRejected", "out of stock");

    private final SentNotificationRepository sent;

    public NotificationService(SentNotificationRepository sent) {
        this.sent = sent;
    }

    @Transactional
    public void handle(String json) {
        JsonNode e = parse(json);
        String type = e.path("type").asText();
        String message = MESSAGES.get(type);
        if (message == null) {
            return; // not a terminal order event we care about
        }
        String eventId = e.path("eventId").asText();
        if (eventId.isEmpty() || sent.existsById(eventId)) {
            return; // at-least-once -> idempotent
        }
        String orderId = e.path("orderId").asText();
        log.info("Sending '{}' notification for order {}", message, orderId);
        sent.save(new SentNotification(eventId, "log", type, orderId, message));
    }

    private JsonNode parse(String json) {
        try {
            return MAPPER.readTree(json);
        } catch (Exception ex) {
            throw new IllegalArgumentException("Invalid event JSON", ex);
        }
    }
}
