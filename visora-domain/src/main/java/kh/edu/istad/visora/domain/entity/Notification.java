package kh.edu.istad.visora.domain.entity;

import kh.edu.istad.visora.domain.entity.enums.NotificationType;
import kh.edu.istad.visora.domain.exception.notification.NotificationValidationException;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Notification extends Entity {

    private long id;

    private String uuid;

    private NotificationType type;

    @ToString.Exclude
    @Builder.Default
    private List<NotificationRecipient> recipients = new ArrayList<>();

    private Object content;

    public void addRecipient(User recipient) {
        if (recipient == null) {
            throw new NotificationValidationException("Notification recipient must not be null");
        }
        boolean alreadyPresent = recipients.stream()
                .anyMatch(existing -> existing.getRecipient() != null
                        && existing.getRecipient().getUuid() != null
                        && existing.getRecipient().getUuid().equals(recipient.getUuid()));
        if (alreadyPresent) {
            throw new NotificationValidationException(
                    "Duplicate recipient in notification: " + recipient.getUuid());
        }
        recipients.add(NotificationRecipient.of(this, recipient));
    }

    public void validate() {
        if (type == null) {
            throw new NotificationValidationException("Notification type is required");
        }
        if (uuid == null || uuid.isBlank()) {
            throw new NotificationValidationException("Notification uuid is required");
        }
        if (recipients == null || recipients.isEmpty()) {
            throw new NotificationValidationException(
                    "A notification must have at least one recipient");
        }
    }

}
