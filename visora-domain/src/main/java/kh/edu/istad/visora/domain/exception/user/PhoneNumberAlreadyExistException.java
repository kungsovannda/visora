package kh.edu.istad.visora.domain.exception.user;

import kh.edu.istad.visora.domain.exception.DomainConflictException;

public class PhoneNumberAlreadyExistException extends DomainConflictException {
    public PhoneNumberAlreadyExistException(String message) {
        super(message);
    }

    public PhoneNumberAlreadyExistException(String message, Throwable cause) {
        super(message, cause);
    }
}
