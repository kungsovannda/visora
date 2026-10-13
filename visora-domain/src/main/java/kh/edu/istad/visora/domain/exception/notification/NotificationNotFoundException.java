package kh.edu.istad.visora.domain.exception.notification;

import kh.edu.istad.visora.domain.exception.DomainNotFoundException;

public class NotificationNotFoundException extends DomainNotFoundException {

    public NotificationNotFoundException(String message) {
        super(message);
    }

    public NotificationNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

}
