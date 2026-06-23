package com.shop.notification.kafka;

import com.shop.notification.service.NotificationService;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class OrderEventListener {

    private final NotificationService service;

    public OrderEventListener(NotificationService service) {
        this.service = service;
    }

    @KafkaListener(topics = "${shop.notification.order-events-topic:order-events}")
    public void onMessage(String value) {
        service.handle(value);
    }
}
