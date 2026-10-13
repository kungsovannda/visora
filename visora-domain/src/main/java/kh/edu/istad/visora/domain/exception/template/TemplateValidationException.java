package kh.edu.istad.visora.domain.exception.template;

import kh.edu.istad.visora.domain.exception.DomainException;

public class TemplateValidationException extends DomainException {

    public TemplateValidationException(String message) {
        super(message);
    }

    public TemplateValidationException(String message, Throwable cause) {
        super(message, cause);
    }

}