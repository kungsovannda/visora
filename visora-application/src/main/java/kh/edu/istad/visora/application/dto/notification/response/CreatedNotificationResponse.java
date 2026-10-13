package kh.edu.istad.visora.application.dto.notification.response;

import kh.edu.istad.visora.domain.entity.enums.NotificationType;

import java.sql.Timestamp;

public record CreatedNotificationResponse(

        String notificationUuid,

        NotificationType type,

        Object content,

        int recipientCount,

        Timestamp createdAt

) {
}
