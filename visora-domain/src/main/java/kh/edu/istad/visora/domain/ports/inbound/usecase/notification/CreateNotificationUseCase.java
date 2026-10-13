package kh.edu.istad.visora.domain.ports.inbound.usecase.notification;

import kh.edu.istad.visora.domain.entity.Notification;
import kh.edu.istad.visora.domain.entity.User;
import kh.edu.istad.visora.domain.entity.enums.NotificationType;
import kh.edu.istad.visora.domain.exception.notification.NotificationValidationException;
import kh.edu.istad.visora.domain.exception.user.UserNotFoundException;
import kh.edu.istad.visora.domain.ports.outbound.repository.NotificationRepository;
import kh.edu.istad.visora.domain.ports.outbound.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CreateNotificationUseCase {

    private final NotificationRepository notificationRepository;
    private final UserRepository userRepository;

    public Notification create(NotificationType type, Object content, List<String> recipientUuids, String recipientRole) {
        boolean hasExplicit = recipientUuids != null && !recipientUuids.isEmpty();
        boolean hasRole = recipientRole != null && !recipientRole.isBlank();

        if (!hasExplicit && !hasRole) {
            throw new NotificationValidationException(
                    "A notification must target at least one recipient: provide recipientUuids or recipientRole");
        }
        if (type == null) {
            throw new NotificationValidationException("Notification type is required");
        }

        Notification notification = Notification.builder()
                .id(0)
                .uuid(UUID.randomUUID().toString())
                .type(type)
                .content(content)
                .recipients(new ArrayList<>())
                .build();

        resolveRecipients(notification, recipientUuids, recipientRole);
        notification.validate();
        return notificationRepository.save(notification);
    }

    private void resolveRecipients(Notification notification, List<String> recipientUuids, String recipientRole) {
        Set<String> seenUuids = new LinkedHashSet<>();

        if (recipientUuids != null) {
            for (String rawUuid : recipientUuids) {
                if (rawUuid == null || rawUuid.isBlank()) {
                    continue;
                }
                String recipientUuid = rawUuid.trim();
                if (!seenUuids.add(recipientUuid)) {
                    continue;
                }
                User user = userRepository.findUserByUuid(recipientUuid)
                        .orElseThrow(() -> new UserNotFoundException("User was not found: " + recipientUuid));
                notification.addRecipient(user);
            }
        }

        if (recipientRole != null && !recipientRole.isBlank()) {
            for (User user : userRepository.findUsersByRole(recipientRole.trim())) {
                String recipientUuid = user.getUuid();
                if (recipientUuid == null || !seenUuids.add(recipientUuid)) {
                    continue;
                }
                notification.addRecipient(user);
            }
        }
    }

}
