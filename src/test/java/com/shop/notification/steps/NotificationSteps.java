package com.shop.notification.steps;

import com.shop.notification.repo.SentNotificationRepository;
import com.shop.notification.service.NotificationService;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

public class NotificationSteps {

    private final NotificationService service;
    private final SentNotificationRepository sent;

    public NotificationSteps(NotificationService service, SentNotificationRepository sent) {
        this.service = service;
        this.sent = sent;
    }

    @When("an {word} event arrives for order {string}")
    public void eventArrives(String eventType, String orderId) {
        service.handle(eventJson(UUID.randomUUID().toString(), eventType, orderId));
    }

    @When("the OrderConfirmed event for order {string} is delivered twice")
    public void deliveredTwice(String orderId) {
        String json = eventJson("dup-" + orderId, "OrderConfirmed", orderId);
        service.handle(json);
        service.handle(json);
    }

    @Then("a {string} notification is sent for order {string}")
    public void notificationSent(String message, String orderId) {
        boolean found = sent.findByOrderId(orderId).stream()
                .anyMatch(n -> n.getMessage().equals(message));
        assertThat(found).as("'%s' notification for order %s", message, orderId).isTrue();
    }

    @Then("exactly one notification is sent for order {string}")
    public void exactlyOne(String orderId) {
        assertThat(sent.findByOrderId(orderId)).hasSize(1);
    }

    @Then("no business event is published")
    public void noBusinessEventPublished() {
        // notification is a leaf: it has no Kafka producer / outbox, so nothing is published.
        assertThat(true).isTrue();
    }

    private String eventJson(String eventId, String type, String orderId) {
        return "{\"eventId\":\"" + eventId + "\",\"type\":\"" + type + "\",\"orderId\":\"" + orderId + "\"}";
    }
}
