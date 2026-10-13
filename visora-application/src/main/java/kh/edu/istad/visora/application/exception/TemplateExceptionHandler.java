package kh.edu.istad.visora.application.exception;

import kh.edu.istad.visora.application.dto.base.ErrorResponse;
import kh.edu.istad.visora.domain.exception.template.TemplateAccessDeniedException;
import kh.edu.istad.visora.domain.exception.template.TemplateValidationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class TemplateExceptionHandler {

    @ExceptionHandler(TemplateAccessDeniedException.class)
    public ResponseEntity<ErrorResponse<?>> handle(TemplateAccessDeniedException ex) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(
                new ErrorResponse<>(
                        HttpStatus.FORBIDDEN.getReasonPhrase(),
                        ex.getMessage(),
                        HttpStatus.FORBIDDEN.value()
                )
        );
    }

    @ExceptionHandler(TemplateValidationException.class)
    public ResponseEntity<ErrorResponse<?>> handle(TemplateValidationException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                new ErrorResponse<>(
                        HttpStatus.BAD_REQUEST.getReasonPhrase(),
                        ex.getMessage(),
                        HttpStatus.BAD_REQUEST.value()
                )
        );
    }

}