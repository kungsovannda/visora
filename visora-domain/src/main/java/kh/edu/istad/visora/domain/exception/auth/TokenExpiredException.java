package kh.edu.istad.visora.domain.exception.auth;

public class TokenExpiredException extends DomainAuthException {
    public TokenExpiredException(String message) {
        super(message);
    }

    public TokenExpiredException(String message, Throwable cause) {
        super(message, cause);
    }
}