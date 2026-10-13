package kh.edu.istad.visora.domain.exception.auth;

import kh.edu.istad.visora.domain.exception.DomainConflictException;

public class EmailNotVerifiedException extends DomainConflictException {
    public EmailNotVerifiedException(String message) {
        super(message);
    }

    public EmailNotVerifiedException(String message, Throwable cause) {
        super(message, cause);
    }
}