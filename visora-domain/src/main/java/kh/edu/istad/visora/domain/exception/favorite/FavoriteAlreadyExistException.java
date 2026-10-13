package kh.edu.istad.visora.domain.exception.favorite;

import kh.edu.istad.visora.domain.exception.DomainConflictException;

public class FavoriteAlreadyExistException extends DomainConflictException {

    public FavoriteAlreadyExistException(String message) {
        super(message);
    }

    public FavoriteAlreadyExistException(String message, Throwable cause) {
        super(message, cause);
    }

}
