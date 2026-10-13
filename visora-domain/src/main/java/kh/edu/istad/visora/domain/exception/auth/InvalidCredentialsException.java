package kh.edu.istad.visora.domain.exception.auth;

public class InvalidCredentialsException extends DomainAuthException {
    public InvalidCredentialsException(String message) {
        super(message);
    }

    public InvalidCredentialsException(String message, Throwable cause) {
        super(message, cause);
    }
}