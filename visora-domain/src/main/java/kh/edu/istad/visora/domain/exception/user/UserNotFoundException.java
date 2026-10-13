package kh.edu.istad.visora.domain.exception.user;

import kh.edu.istad.visora.domain.exception.DomainNotFoundException;

public class UserNotFoundException extends DomainNotFoundException {
    public UserNotFoundException(String message) {
        super(message);
    }

    public UserNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
