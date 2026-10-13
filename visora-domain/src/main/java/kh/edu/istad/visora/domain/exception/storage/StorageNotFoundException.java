package kh.edu.istad.visora.domain.exception.storage;

import kh.edu.istad.visora.domain.exception.DomainNotFoundException;

public class StorageNotFoundException extends DomainNotFoundException {
    public StorageNotFoundException(String message) {
        super(message);
    }

    public StorageNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}