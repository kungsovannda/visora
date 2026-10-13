package kh.edu.istad.visora.domain.exception.backdrop;

import kh.edu.istad.visora.domain.exception.DomainConflictException;

public class BackdropVersionConflictException extends DomainConflictException {

    public BackdropVersionConflictException(String message) {
        super(message);
    }

    public BackdropVersionConflictException(String message, Throwable cause) {
        super(message, cause);
    }

}