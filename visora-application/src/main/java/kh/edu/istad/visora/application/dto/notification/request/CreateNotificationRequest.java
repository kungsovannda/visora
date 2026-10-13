package kh.edu.istad.visora.application.dto.notification.request;

import kh.edu.istad.visora.domain.entity.enums.NotificationType;

import java.util.List;

public record CreateNotificationRequest(

        NotificationType type,

        Object content,

        List<String> recipientUuids,

        String recipientRole

) {
}
