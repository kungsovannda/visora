package kh.edu.istad.visora.domain.ports.outbound.repository;

import kh.edu.istad.visora.domain.dto.request.NotificationFilter;
import kh.edu.istad.visora.domain.dto.response.Page;
import kh.edu.istad.visora.domain.entity.Notification;
import kh.edu.istad.visora.domain.entity.NotificationRecipient;
import kh.edu.istad.visora.domain.entity.User;

import java.util.Optional;

public interface NotificationRepository {

    Notification save(Notification notification);

    Optional<Notification> findByUuid(String uuid);

    Page<NotificationRecipient> findForRecipient(User recipient, NotificationFilter filter);

    Optional<NotificationRecipient> findRecipientRow(String notificationUuid, User recipient);

    long countUnreadForRecipient(User recipient);

    NotificationRecipient saveRecipient(NotificationRecipient notificationRecipient);

    int markAllReadForRecipient(User recipient);

}
