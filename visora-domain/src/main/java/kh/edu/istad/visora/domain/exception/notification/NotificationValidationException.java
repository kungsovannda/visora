package kh.edu.istad.visora.domain.exception.notification;

import kh.edu.istad.visora.domain.exception.DomainException;

public class NotificationValidationException extends DomainException {

    public NotificationValidationException(String message) {
        super(message);
    }

    public NotificationValidationException(String message, Throwable cause) {
        super(message, cause);
    }

}
