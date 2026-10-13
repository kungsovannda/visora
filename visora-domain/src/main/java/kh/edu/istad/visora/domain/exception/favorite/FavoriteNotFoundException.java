package kh.edu.istad.visora.domain.exception.favorite;

import kh.edu.istad.visora.domain.exception.DomainNotFoundException;

public class FavoriteNotFoundException extends DomainNotFoundException {

    public FavoriteNotFoundException(String message) {
        super(message);
    }

    public FavoriteNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

}
