package kh.edu.istad.visora.application.exception;

import kh.edu.istad.visora.application.dto.base.ErrorResponse;
import kh.edu.istad.visora.domain.exception.favorite.FavoriteValidationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class FavoriteExceptionHandler {

    @ExceptionHandler(FavoriteValidationException.class)
    public ResponseEntity<ErrorResponse<?>> handle(FavoriteValidationException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                new ErrorResponse<>(
                        HttpStatus.BAD_REQUEST.getReasonPhrase(),
                        ex.getMessage(),
                        HttpStatus.BAD_REQUEST.value()
                )
        );
    }

}
