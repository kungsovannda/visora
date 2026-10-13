package kh.edu.istad.visora.domain.exception.template;

import kh.edu.istad.visora.domain.exception.DomainConflictException;

public class TemplateAlreadyPendingException extends DomainConflictException {

    public TemplateAlreadyPendingException(String message) {
        super(message);
    }

    public TemplateAlreadyPendingException(String message, Throwable cause) {
        super(message, cause);
    }

}