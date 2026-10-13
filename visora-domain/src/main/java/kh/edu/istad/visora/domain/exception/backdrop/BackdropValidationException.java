package kh.edu.istad.visora.domain.exception.backdrop;

import kh.edu.istad.visora.domain.exception.DomainException;

public class BackdropValidationException extends DomainException {

    public BackdropValidationException(String message) {
        super(message);
    }

    public BackdropValidationException(String message, Throwable cause) {
        super(message, cause);
    }

}