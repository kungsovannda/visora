package kh.edu.istad.visora.application.exception;

import kh.edu.istad.visora.application.dto.base.ErrorResponse;
import kh.edu.istad.visora.domain.exception.notification.NotificationNotFoundException;
import kh.edu.istad.visora.domain.exception.notification.NotificationValidationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class NotificationExceptionHandler {

    @ExceptionHandler(NotificationValidationException.class)
    public ResponseEntity<ErrorResponse<?>> handle(NotificationValidationException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                new ErrorResponse<>(
                        HttpStatus.BAD_REQUEST.getReasonPhrase(),
                        ex.getMessage(),
                        HttpStatus.BAD_REQUEST.value()
                )
        );
    }

    @ExceptionHandler(NotificationNotFoundException.class)
    public ResponseEntity<ErrorResponse<?>> handle(NotificationNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                new ErrorResponse<>(
                        HttpStatus.NOT_FOUND.getReasonPhrase(),
                        ex.getMessage(),
                        HttpStatus.NOT_FOUND.value()
                )
        );
    }

}
