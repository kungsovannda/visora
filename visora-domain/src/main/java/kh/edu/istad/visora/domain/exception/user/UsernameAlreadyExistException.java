package kh.edu.istad.visora.domain.exception.user;

import kh.edu.istad.visora.domain.exception.DomainConflictException;

public class UsernameAlreadyExistException extends DomainConflictException {
    public UsernameAlreadyExistException(String message) {
        super(message);
    }

    public UsernameAlreadyExistException(String message, Throwable cause) {
        super(message, cause);
    }
}
