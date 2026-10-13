package kh.edu.istad.visora.application.exception;

import kh.edu.istad.visora.application.dto.base.ErrorResponse;
import kh.edu.istad.visora.domain.exception.auth.DomainAuthException;
import kh.edu.istad.visora.domain.exception.auth.PasswordNotMatchException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class AuthExceptionHandler {

    @ExceptionHandler(PasswordNotMatchException.class)
    public ResponseEntity<ErrorResponse<?>> handle(PasswordNotMatchException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                new ErrorResponse<>(
                        HttpStatus.BAD_REQUEST.getReasonPhrase(),
                        ex.getMessage(),
                        HttpStatus.BAD_REQUEST.value()
                )
        );
    }

    @ExceptionHandler(DomainAuthException.class)
    public ResponseEntity<ErrorResponse<?>> handle(DomainAuthException ex) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(
                new ErrorResponse<>(
                        HttpStatus.UNAUTHORIZED.getReasonPhrase(),
                        ex.getMessage(),
                        HttpStatus.UNAUTHORIZED.value()
                )
        );
    }

}