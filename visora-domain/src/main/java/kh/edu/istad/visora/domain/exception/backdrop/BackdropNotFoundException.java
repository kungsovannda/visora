package kh.edu.istad.visora.domain.exception.backdrop;

import kh.edu.istad.visora.domain.exception.DomainNotFoundException;

public class BackdropNotFoundException extends DomainNotFoundException {

    public BackdropNotFoundException(String message) {
        super(message);
    }

    public BackdropNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

}