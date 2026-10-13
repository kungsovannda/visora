package kh.edu.istad.visora.domain.exception.backdrop;

import kh.edu.istad.visora.domain.exception.DomainException;

public class BackdropAccessDeniedException extends DomainException {

    public BackdropAccessDeniedException(String message) {
        super(message);
    }

    public BackdropAccessDeniedException(String message, Throwable cause) {
        super(message, cause);
    }

}