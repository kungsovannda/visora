package kh.edu.istad.visora.domain.ports.inbound.usecase.notification;

import kh.edu.istad.visora.domain.dto.request.NotificationFilter;
import kh.edu.istad.visora.domain.dto.response.Page;
import kh.edu.istad.visora.domain.entity.Notification;
import kh.edu.istad.visora.domain.entity.NotificationRecipient;
import kh.edu.istad.visora.domain.entity.User;
import kh.edu.istad.visora.domain.exception.notification.NotificationNotFoundException;
import kh.edu.istad.visora.domain.exception.notification.NotificationValidationException;
import kh.edu.istad.visora.domain.ports.CurrentUser;
import kh.edu.istad.visora.domain.ports.outbound.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class QueryNotificationUseCase {

    private final NotificationRepository notificationRepository;
    private final CurrentUser currentUser;

    public Page<NotificationRecipient> getMyNotifications(NotificationFilter filter) {
        return notificationRepository.findForRecipient(requireCurrentUser(), filter);
    }

    public Notification getByUuid(String uuid) {
        return notificationRepository.findByUuid(uuid)
                .orElseThrow(() -> new NotificationNotFoundException("Notification was not found"));
    }

    public long getUnreadCount() {
        return notificationRepository.countUnreadForRecipient(requireCurrentUser());
    }

    private User requireCurrentUser() {
        User user = currentUser.get();
        if (user == null) {
            throw new NotificationValidationException("You must be authenticated to view notifications");
        }
        return user;
    }

}
