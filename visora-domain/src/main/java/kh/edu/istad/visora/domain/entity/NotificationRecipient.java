package kh.edu.istad.visora.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.sql.Timestamp;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NotificationRecipient extends Entity {

    private long id;

    @ToString.Exclude
    private Notification notification;

    private User recipient;

    private boolean read;

    private Timestamp readAt;

    public static NotificationRecipient of(Notification notification, User recipient) {
        return NotificationRecipient.builder()
                .notification(notification)
                .recipient(recipient)
                .read(false)
                .build();
    }

    public void markAsRead() {
        if (!this.read) {
            this.read = true;
            this.readAt = new Timestamp(System.currentTimeMillis());
        }
    }

}
