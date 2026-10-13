package kh.edu.istad.visora.application.exception;

import kh.edu.istad.visora.application.dto.base.ErrorResponse;
import org.jspecify.annotations.NonNull;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;

import java.util.*;

@RestControllerAdvice
public class ValidationExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ErrorResponse<?>> handle(MethodArgumentNotValidException ex) {
        return getErrorResponseResponseEntity(ex);
    }

    @ExceptionHandler(BindException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ErrorResponse<?>> handle(BindException ex) {
        return getErrorResponseResponseEntity(ex);
    }

    @NonNull
    private ResponseEntity<ErrorResponse<?>> getErrorResponseResponseEntity(BindException ex) {
        ErrorResponse.ErrorResponseBuilder<Object> builder = ErrorResponse.builder();
        List<Map<String, Object>> errors = new ArrayList<>();

        for (FieldError fieldError : ex.getBindingResult().getFieldErrors()) {
            Map<String, Object> error = new HashMap<>();
            error.put("field", fieldError.getField());
            error.put("reason", fieldError.getDefaultMessage());
            errors.add(error);
        }

        for (ObjectError objectError : ex.getBindingResult().getGlobalErrors()) {
            Map<String, Object> error = new HashMap<>();
            error.put("object", objectError.getObjectName());
            error.put("reason", objectError.getDefaultMessage());
            errors.add(error);
        }

        builder.message(HttpStatus.BAD_REQUEST.getReasonPhrase());
        builder.status(HttpStatus.BAD_REQUEST.value());
        builder.detail(errors);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(builder.build());
    }

    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ErrorResponse<?>> handle(ConstraintViolationException ex) {
        ErrorResponse.ErrorResponseBuilder<Object> builder = ErrorResponse.builder();
        List<Map<String, Object>> errors = new ArrayList<>();

        for (ConstraintViolation<?> violation : ex.getConstraintViolations()) {
            Map<String, Object> error = new HashMap<>();
            String fieldPath = cleanFieldPath(violation.getPropertyPath().toString());
            error.put("field", fieldPath);
            error.put("reason", violation.getMessage());
            error.put("invalidValue", violation.getInvalidValue());
            errors.add(error);
        }

        builder.message(HttpStatus.BAD_REQUEST.getReasonPhrase());
        builder.status(HttpStatus.BAD_REQUEST.value());
        builder.detail(errors);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(builder.build());
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ErrorResponse<?>> handle(HttpMessageNotReadableException ex) {
        ErrorResponse.ErrorResponseBuilder<Object> builder = ErrorResponse.builder();
        builder.message(HttpStatus.BAD_REQUEST.getReasonPhrase());
        builder.status(HttpStatus.BAD_REQUEST.value());
        builder.detail("Invalid JSON format or structure: " + ex.getMostSpecificCause().getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(builder.build());
    }

    private String cleanFieldPath(String fieldPath) {
        if (fieldPath == null || fieldPath.isEmpty()) {
            return fieldPath;
        }

        String[] segments = fieldPath.split("\\.");

        if (segments.length < 3) {
            return fieldPath;
        }

        List<String> cleanSegments = new ArrayList<>(Arrays.asList(segments).subList(2, segments.length));

        return String.join(".", cleanSegments);
    }
}