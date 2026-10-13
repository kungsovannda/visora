package kh.edu.istad.visora.domain.exception.template;

import kh.edu.istad.visora.domain.exception.DomainConflictException;

public class TemplateVersionConflictException extends DomainConflictException {

    public TemplateVersionConflictException(String message) {
        super(message);
    }

    public TemplateVersionConflictException(String message, Throwable cause) {
        super(message, cause);
    }

}