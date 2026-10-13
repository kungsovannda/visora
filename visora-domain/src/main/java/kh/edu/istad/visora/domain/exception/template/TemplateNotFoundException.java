package kh.edu.istad.visora.domain.exception.template;

import kh.edu.istad.visora.domain.exception.DomainNotFoundException;

public class TemplateNotFoundException extends DomainNotFoundException {

    public TemplateNotFoundException(String message) {
        super(message);
    }

    public TemplateNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

}