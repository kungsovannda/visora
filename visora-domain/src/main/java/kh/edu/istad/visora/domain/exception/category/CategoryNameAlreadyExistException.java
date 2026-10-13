package kh.edu.istad.visora.domain.exception.category;

import kh.edu.istad.visora.domain.exception.DomainConflictException;

public class CategoryNameAlreadyExistException extends DomainConflictException {
    public CategoryNameAlreadyExistException(String message) {
        super(message);
    }

    public CategoryNameAlreadyExistException(String message, Throwable cause) {
        super(message, cause);
    }
}