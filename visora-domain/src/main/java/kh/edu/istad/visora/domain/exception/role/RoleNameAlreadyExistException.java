package kh.edu.istad.visora.domain.exception.role;

import kh.edu.istad.visora.domain.exception.DomainConflictException;

public class RoleNameAlreadyExistException extends DomainConflictException {
    public RoleNameAlreadyExistException(String message) {
        super(message);
    }

    public RoleNameAlreadyExistException(String message, Throwable cause) {
        super(message, cause);
    }
}
