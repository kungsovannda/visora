package kh.edu.istad.visora.domain.ports.inbound.usecase.notification;

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
public class MarkNotificationReadUseCase {

    private final NotificationRepository notificationRepository;
    private final CurrentUser currentUser;

    public NotificationRecipient markAsRead(String notificationUuid) {
        User user = requireCurrentUser();
        NotificationRecipient row = notificationRepository.findRecipientRow(notificationUuid, user)
                .orElseThrow(() -> new NotificationNotFoundException("Notification was not found"));
        row.markAsRead();
        return notificationRepository.saveRecipient(row);
    }

    public int markAllAsRead() {
        return notificationRepository.markAllReadForRecipient(requireCurrentUser());
    }

    private User requireCurrentUser() {
        User user = currentUser.get();
        if (user == null) {
            throw new NotificationValidationException("You must be authenticated to manage notifications");
        }
        return user;
    }

}
