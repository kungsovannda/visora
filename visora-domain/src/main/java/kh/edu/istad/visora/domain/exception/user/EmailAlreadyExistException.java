package kh.edu.istad.visora.domain.exception.user;

import kh.edu.istad.visora.domain.exception.DomainConflictException;

public class EmailAlreadyExistException extends DomainConflictException {
    public EmailAlreadyExistException(String message) {
        super(message);
    }

    public EmailAlreadyExistException(String message, Throwable cause) {
        super(message, cause);
    }
}
