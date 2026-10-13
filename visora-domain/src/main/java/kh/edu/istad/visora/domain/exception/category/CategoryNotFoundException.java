package kh.edu.istad.visora.domain.exception.category;

import kh.edu.istad.visora.domain.exception.DomainNotFoundException;

public class CategoryNotFoundException extends DomainNotFoundException {
    public CategoryNotFoundException(String message) {
        super(message);
    }

    public CategoryNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
