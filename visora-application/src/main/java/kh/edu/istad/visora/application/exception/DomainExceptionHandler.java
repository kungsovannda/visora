package kh.edu.istad.visora.application.exception;

import kh.edu.istad.visora.application.dto.base.ErrorResponse;
import kh.edu.istad.visora.domain.exception.DomainConflictException;
import kh.edu.istad.visora.domain.exception.DomainException;
import kh.edu.istad.visora.domain.exception.DomainNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class DomainExceptionHandler {

    @ExceptionHandler(DomainConflictException.class)
    public ResponseEntity<ErrorResponse<?>> handle(DomainConflictException ex){
        return ResponseEntity.status(HttpStatus.CONFLICT).body(
                new ErrorResponse<>(
                        HttpStatus.CONFLICT.getReasonPhrase(),
                        ex.getMessage(),
                        HttpStatus.CONFLICT.value()
                )
        );
    }

    @ExceptionHandler(DomainNotFoundException.class)
    public ResponseEntity<ErrorResponse<?>> handle(DomainNotFoundException ex){
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                new ErrorResponse<>(
                        HttpStatus.NOT_FOUND.getReasonPhrase(),
                        ex.getMessage(),
                        HttpStatus.NOT_FOUND.value()
                )
        );
    }

    @ExceptionHandler(DomainException.class)
    public ResponseEntity<ErrorResponse<?>> handle(DomainException ex){
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                new ErrorResponse<>(
                        HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(),
                        ex.getMessage(),
                        HttpStatus.INTERNAL_SERVER_ERROR.value()
                )
        );
    }

}
