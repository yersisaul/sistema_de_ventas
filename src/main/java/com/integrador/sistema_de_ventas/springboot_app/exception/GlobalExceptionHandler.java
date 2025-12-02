package com.integrador.sistema_de_ventas.springboot_app.exception;

import com.integrador.sistema_de_ventas.springboot_app.dto.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

@RestControllerAdvice
public class GlobalExceptionHandler {
    
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<?> handleResourceNotFoundException(
        ResourceNotFoundException ex, WebRequest request) {
        ApiResponse<?> response = new ApiResponse<>(
            false,
            ex.getMessage(),
            null
        );
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }
    
    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<?> handleBadRequestException(
        BadRequestException ex, WebRequest request) {
        ApiResponse<?> response = new ApiResponse<>(
            false,
            ex.getMessage(),
            null
        );
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }
    
    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<?> handleUnauthorizedException(
        UnauthorizedException ex, WebRequest request) {
        ApiResponse<?> response = new ApiResponse<>(
            false,
            ex.getMessage(),
            null
        );
        return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
    }
    
    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleGlobalException(
        Exception ex, WebRequest request) {
        ApiResponse<?> response = new ApiResponse<>(
            false,
            "Error interno del servidor: " + ex.getMessage(),
            null
        );
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
