package kh.edu.istad.visora.domain.exception;

public class SlugAlreadyExistException extends DomainConflictException {
    public SlugAlreadyExistException(String message) {
        super(message);
    }

    public SlugAlreadyExistException(String message, Throwable cause) {
        super(message, cause);
    }
}
