package kh.edu.istad.visora.domain.exception.template;

import kh.edu.istad.visora.domain.exception.DomainNotFoundException;

public class NoPendingTemplateException extends DomainNotFoundException {

    public NoPendingTemplateException(String message) {
        super(message);
    }

    public NoPendingTemplateException(String message, Throwable cause) {
        super(message, cause);
    }

}