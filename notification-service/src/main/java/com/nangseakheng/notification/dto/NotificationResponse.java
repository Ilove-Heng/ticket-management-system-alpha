package com.nangseakheng.notification.dto;

import com.nangseakheng.notification.enumz.NotificationStatus;
import com.nangseakheng.notification.enumz.NotificationType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NotificationResponse {

    private Long id;
    private String username;
    private String email;
    private Long orderId;
    private String eventType;
    private NotificationType notificationType;
    private String recipient;
    private String subject;
    private String message;
    private NotificationStatus status;
}
