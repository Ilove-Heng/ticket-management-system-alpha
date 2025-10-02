package com.nangseakheng.notification.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nangseakheng.notification.dto.OrderConfirmedEvent;
import com.nangseakheng.notification.service.NotificationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class OrderConfirmedEventListener {
    private final NotificationService notificationService;
    private final ObjectMapper objectMapper;

    public OrderConfirmedEventListener(NotificationService notificationService, ObjectMapper objectMapper) {
        this.notificationService = notificationService;
        this.objectMapper = objectMapper;
    }


    @KafkaListener(topics = "${notification.topic.order-confirmed}", groupId = "${spring.kafka.consumer.group-id}")
    public void handleOrderConfirmedEvent(String message) {
        log.info("Received OrderConfirmedEvent: {}", message);
        try {
            OrderConfirmedEvent orderConfirmedEvent = objectMapper.readValue(message, OrderConfirmedEvent.class);
            notificationService.handlerOrderConfirmedEvent(orderConfirmedEvent);
            log.info("Successfully processed OrderConfirmedEvent for orderId: {}", orderConfirmedEvent);
        } catch (Exception e) {
            log.error("Error processing OrderConfirmedEvent: {}", e.getMessage(), e);
            throw new RuntimeException(e);
        }
    }
}
