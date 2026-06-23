package com.shop.notification.steps;

import com.shop.notification.repo.SentNotificationRepository;
import io.cucumber.java.Before;

public class CleanupHooks {

    private final SentNotificationRepository sent;

    public CleanupHooks(SentNotificationRepository sent) {
        this.sent = sent;
    }

    @Before
    public void clean() {
        sent.deleteAll();
    }
}
