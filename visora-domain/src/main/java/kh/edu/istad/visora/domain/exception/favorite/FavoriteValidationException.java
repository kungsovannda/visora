package kh.edu.istad.visora.domain.exception.favorite;

import kh.edu.istad.visora.domain.exception.DomainException;

public class FavoriteValidationException extends DomainException {

    public FavoriteValidationException(String message) {
        super(message);
    }

    public FavoriteValidationException(String message, Throwable cause) {
        super(message, cause);
    }

}
