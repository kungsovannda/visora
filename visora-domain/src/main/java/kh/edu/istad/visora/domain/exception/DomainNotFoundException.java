package kh.edu.istad.visora.domain.exception;

public class DomainNotFoundException extends DomainException {
    public DomainNotFoundException(String message) {
        super(message);
    }

    public DomainNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
