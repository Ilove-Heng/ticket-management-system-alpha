package com.nangseakheng.notification.service;

import com.nangseakheng.notification.dto.NotificationRequest;
import com.nangseakheng.notification.dto.NotificationResponse;
import com.nangseakheng.notification.dto.OrderConfirmedEvent;

public interface NotificationService {
    void handlerOrderConfirmedEvent(OrderConfirmedEvent orderConfirmedEvent);
    NotificationResponse sendNotification(NotificationRequest notificationRequest);
}
