package kh.edu.istad.visora.application.dto.notification.response;

import kh.edu.istad.visora.domain.entity.enums.NotificationType;

import java.sql.Timestamp;

public record NotificationResponse(

        String notificationUuid,

        NotificationType type,

        Object content,

        boolean read,

        Timestamp readAt,

        Timestamp createdAt

) {
}
