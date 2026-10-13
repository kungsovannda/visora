package kh.edu.istad.visora.domain.exception.role;

import kh.edu.istad.visora.domain.exception.DomainNotFoundException;

public class RoleNotFoundException extends DomainNotFoundException {
    public RoleNotFoundException(String message) {
        super(message);
    }

    public RoleNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
