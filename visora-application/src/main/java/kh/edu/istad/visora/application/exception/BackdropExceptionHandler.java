package kh.edu.istad.visora.application.exception;

import kh.edu.istad.visora.application.dto.base.ErrorResponse;
import kh.edu.istad.visora.domain.exception.backdrop.BackdropAccessDeniedException;
import kh.edu.istad.visora.domain.exception.backdrop.BackdropValidationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class BackdropExceptionHandler {

    @ExceptionHandler(BackdropAccessDeniedException.class)
    public ResponseEntity<ErrorResponse<?>> handle(BackdropAccessDeniedException ex) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(
                new ErrorResponse<>(
                        HttpStatus.FORBIDDEN.getReasonPhrase(),
                        ex.getMessage(),
                        HttpStatus.FORBIDDEN.value()
                )
        );
    }

    @ExceptionHandler(BackdropValidationException.class)
    public ResponseEntity<ErrorResponse<?>> handle(BackdropValidationException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                new ErrorResponse<>(
                        HttpStatus.BAD_REQUEST.getReasonPhrase(),
                        ex.getMessage(),
                        HttpStatus.BAD_REQUEST.value()
                )
        );
    }

}