package com.nangseakheng.notification.service.impl;

import com.nangseakheng.notification.dto.NotificationRequest;
import com.nangseakheng.notification.dto.NotificationResponse;
import com.nangseakheng.notification.dto.OrderConfirmedEvent;
import com.nangseakheng.notification.entity.Notification;
import com.nangseakheng.notification.enumz.NotificationStatus;
import com.nangseakheng.notification.enumz.NotificationType;
import com.nangseakheng.notification.repository.NotificationRepository;
import com.nangseakheng.notification.service.EmailService;
import com.nangseakheng.notification.service.NotificationService;
import com.nangseakheng.notification.service.SmsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
@Slf4j
public class NotificationServiceImpl implements NotificationService {

    private final EmailService emailService;
    private final SmsService smsService;
    private final NotificationRepository notificationRepository;

    public NotificationServiceImpl(EmailService emailService,SmsService smsService, NotificationRepository notificationRepository) {
        this.emailService = emailService;
        this.smsService = smsService;
        this.notificationRepository = notificationRepository;
    }

    @Override
    public void handlerOrderConfirmedEvent(OrderConfirmedEvent confirmedEvent) {
        try {
            if (StringUtils.hasText(confirmedEvent.getEmail())) {
                // TODO: send email logic here
                sendOrderConfirmationEmail(confirmedEvent);
            }
            if (StringUtils.hasText(confirmedEvent.getPhoneNumber())) {
                // TODO: send SMS logic here
                sendOrderConfirmationSms(confirmedEvent);
                log.info("SMS notification to {} is not implemented yet.", confirmedEvent.getPhoneNumber());
            }

        } catch (Exception e) {
            log.error("Failed to handle order confirmed event: {}", e.getMessage());
        }
    }

    @Override
    public NotificationResponse sendNotification(NotificationRequest notificationRequest) {
        Notification notification = toEntity(notificationRequest);
        boolean sent = false;

        try {
            sent = switch (notificationRequest.getNotificationType()) {
                case EMAIL -> emailService.sendEmail(
                        notificationRequest.getRecipient(),
                        notificationRequest.getSubject(),
                        notificationRequest.getMessage()
                );
                case SMS -> smsService.sendSms(
                        notificationRequest.getRecipient(),
                        notificationRequest.getMessage()
                );
                case PUSH_NOTIFICATION -> false; // create new service for push notification
            };

        } catch(Exception e) {
            notification.setErrorMessage(e.getMessage());
            log.error("Failed to send notification: {}", e.getMessage());
        }

        updateNotificationStatus(notification, sent);

        return toResponse(notification);
    }

    private void sendOrderConfirmationEmail(OrderConfirmedEvent confirmedEvent) {
        String subject = "Ticket Order Confirmation - " + confirmedEvent.getEventTitle();
        String message = emailService.generateOrderConfirmationEmailBody(
                confirmedEvent.getUsername(),
                confirmedEvent.getEventTitle(),
                confirmedEvent.getEventDate(),
                confirmedEvent.getEventLocation(),
                confirmedEvent.getQuantity(),
                confirmedEvent.getAmount()
        );

        NotificationRequest notificationRequest = NotificationRequest.builder()
                .username(confirmedEvent.getUsername())
                .orderId(confirmedEvent.getOrderId())
                .eventType("ORDER_CONFIRMATION")
                .notificationType(NotificationType.EMAIL)
                .recipient(confirmedEvent.getEmail())
                .subject(subject)
                .message(message)
                .build();

        Notification notification = toEntity(notificationRequest);
        notificationRepository.save(notification);

        boolean sent = emailService.sendEmail(
                notificationRequest.getRecipient(),
                notificationRequest.getSubject(),
                notificationRequest.getMessage()
        );

        updateNotificationStatus(notification, sent);
    }

    private void sendOrderConfirmationSms(OrderConfirmedEvent confirmedEvent) {
        String message = smsService.generateOrderConfirmationSmsBody(
                confirmedEvent.getEventTitle(),
                confirmedEvent.getEventDate(),
                confirmedEvent.getEventLocation(),
                confirmedEvent.getQuantity()
        );

        NotificationRequest notificationRequest = NotificationRequest.builder()
                .username(confirmedEvent.getUsername())
                .orderId(confirmedEvent.getOrderId())
                .eventType("ORDER_CONFIRMATION")
                .notificationType(NotificationType.SMS)
                .recipient(confirmedEvent.getPhoneNumber())
                .subject(null) // SMS typically doesn't have a subject
                .message(message)
                .build();

        Notification notification = toEntity(notificationRequest);
        notificationRepository.save(notification);

        boolean sent = smsService.sendSms(
                notificationRequest.getRecipient(),
                notificationRequest.getMessage()
        );

        updateNotificationStatus(notification, sent);
    }

    private void updateNotificationStatus(Notification notification, boolean sent) {

        notification.setStatus(NotificationStatus.FAILED);
        if(sent) {
            notification.setStatus(NotificationStatus.SENT);
        }
        notificationRepository.save(notification);
    }

    public Notification toEntity(NotificationRequest notificationRequest) {
        return Notification.builder()
                .username(notificationRequest.getUsername())
                .orderId(notificationRequest.getOrderId())
                .eventType(notificationRequest.getEventType())
                .notificationType(notificationRequest.getNotificationType())
                .recipient(notificationRequest.getRecipient())
                .subject(notificationRequest.getSubject())
                .message(notificationRequest.getMessage())
                .build();
    }

    public NotificationResponse toResponse(Notification notification) {
        return NotificationResponse.builder()
                .id(notification.getId())
                .username(notification.getUsername())
                .orderId(notification.getOrderId())
                .eventType(notification.getEventType())
                .notificationType(notification.getNotificationType())
                .recipient(notification.getRecipient())
                .subject(notification.getSubject())
                .message(notification.getMessage())
                .status(notification.getStatus())
                .build();
    }
}
