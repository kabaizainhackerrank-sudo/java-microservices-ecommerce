package com.dennis.ecommerce.auth.exception;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // Errores de validación (@Valid)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleValidationErrors(
            MethodArgumentNotValidException ex) {

        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach(error -> {
            String field = ((FieldError) error).getField();
            String message = error.getDefaultMessage();
            errors.put(field, message);
        });

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                buildResponse("Errores de validación", HttpStatus.BAD_REQUEST, errors)
        );
    }

    // Errores de autenticación/negocio
    @ExceptionHandler(AuthException.class)
    public ResponseEntity<Map<String, Object>> handleAuthException(AuthException ex) {
        return ResponseEntity.status(ex.getStatus()).body(
                buildResponse(ex.getMessage(), ex.getStatus(), null)
        );
    }

    // Cualquier otro error no controlado
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleGenericException(Exception ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                buildResponse("Error interno del servidor", HttpStatus.INTERNAL_SERVER_ERROR, null)
        );
    }

    private Map<String, Object> buildResponse(String message, HttpStatus status, Object details) {
        Map<String, Object> body = new HashMap<>();
        body.put("timestamp", LocalDateTime.now().toString());
        body.put("status", status.value());
        body.put("message", message);
        if (details != null) {
            body.put("details", details);
        }
        return body;
    }
}
