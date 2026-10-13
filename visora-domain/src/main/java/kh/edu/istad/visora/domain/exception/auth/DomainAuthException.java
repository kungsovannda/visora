package kh.edu.istad.visora.domain.exception.auth;

import kh.edu.istad.visora.domain.exception.DomainException;

public class DomainAuthException extends DomainException {
    public DomainAuthException(String message) {
        super(message);
    }

    public DomainAuthException(String message, Throwable cause) {
        super(message, cause);
    }
}
