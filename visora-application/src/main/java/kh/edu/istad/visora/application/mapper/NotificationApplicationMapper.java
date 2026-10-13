package kh.edu.istad.visora.application.mapper;

import kh.edu.istad.visora.application.dto.notification.response.CreatedNotificationResponse;
import kh.edu.istad.visora.application.dto.notification.response.NotificationResponse;
import kh.edu.istad.visora.domain.entity.Notification;
import kh.edu.istad.visora.domain.entity.NotificationRecipient;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface NotificationApplicationMapper {

    @Mapping(target = "notificationUuid", source = "notification.uuid")
    @Mapping(target = "type", source = "notification.type")
    @Mapping(target = "content", source = "notification.content")
    @Mapping(target = "createdAt", source = "notification.createdAt")
    NotificationResponse toResponse(NotificationRecipient notificationRecipient);

    @Mapping(target = "notificationUuid", source = "uuid")
    @Mapping(target = "recipientCount", expression = "java(notification.getRecipients() == null ? 0 : notification.getRecipients().size())")
    CreatedNotificationResponse toCreatedResponse(Notification notification);

}
