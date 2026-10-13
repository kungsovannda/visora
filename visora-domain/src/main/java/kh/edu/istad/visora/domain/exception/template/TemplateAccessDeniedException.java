package kh.edu.istad.visora.domain.exception.template;

import kh.edu.istad.visora.domain.exception.DomainException;

public class TemplateAccessDeniedException extends DomainException {

    public TemplateAccessDeniedException(String message) {
        super(message);
    }

    public TemplateAccessDeniedException(String message, Throwable cause) {
        super(message, cause);
    }

}