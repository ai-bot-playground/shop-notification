package com.shop.notification.repo;

import com.shop.notification.domain.SentNotification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SentNotificationRepository extends JpaRepository<SentNotification, String> {

    List<SentNotification> findByOrderId(String orderId);
}
