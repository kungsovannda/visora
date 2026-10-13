package kh.edu.istad.visora.domain.exception.auth;

public class InvalidTokenException extends DomainAuthException {
    public InvalidTokenException(String message) {
        super(message);
    }

    public InvalidTokenException(String message, Throwable cause) {
        super(message, cause);
    }
}