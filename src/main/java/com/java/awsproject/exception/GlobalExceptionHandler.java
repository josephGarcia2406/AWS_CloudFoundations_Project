package com.java.awsproject.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {
    // Maneja errores de validación (campos vacíos o tipo de dato incorrecto)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors()
                .forEach(error -> errors.put(error.getField(), error.getDefaultMessage()));
        // Devuelve 400 Bad Request
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
    }

    // Maneja recurso o ruta no encontrada (404)
    @ExceptionHandler({
        org.springframework.web.servlet.NoHandlerFoundException.class,
        org.springframework.web.servlet.resource.NoResourceFoundException.class
    })
    public ResponseEntity<Map<String, String>> handleNotFoundExceptions(Exception ex) {
        Map<String, String> error = new HashMap<>();
        error.put("error", "Recurso no encontrado: " + ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }

    // Maneja método no soportado (405)
    @ExceptionHandler(org.springframework.web.HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<Map<String, String>> handleMethodNotAllowedExceptions(Exception ex) {
        Map<String, String> error = new HashMap<>();
        error.put("error", "Método no soportado: " + ex.getMessage());
        return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED).body(error);
    }

    // Maneja cualquier otro error no contemplado (500)
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, String>> handleAllExceptions(Exception ex) {
        Map<String, String> error = new HashMap<>();
        error.put("error", "Error interno del servidor: " + ex.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error); // Devuelve 500
    }
}
