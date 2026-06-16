package com.riwi.hamilton.exception.handler;

import com.riwi.hamilton.exception.BusinessRuleViolationException;
import com.riwi.hamilton.exception.DuplicateResourceException;
import com.riwi.hamilton.exception.ResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.net.URI;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // 1. Manejo de Recurso No Encontrado (404)
    @ExceptionHandler(ResourceNotFoundException.class)
    public ProblemDetail handleResourceNotFoundException(ResourceNotFoundException ex, WebRequest request) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, ex.getMessage());
        problemDetail.setTitle("Recurso No Encontrado");
        problemDetail.setType(URI.create("https://api.tuapp.com/errors/not-found"));
        return problemDetail;
    }

    // 2. Manejo de Recurso Duplicado (409 Conflict)
    @ExceptionHandler(DuplicateResourceException.class)
    public ProblemDetail handleDuplicateResourceException(DuplicateResourceException ex) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.CONFLICT, ex.getMessage());
        problemDetail.setTitle("Recurso Duplicado");
        return problemDetail;
    }

    // 3. Manejo de Violación de Reglas de Negocio (422 Unprocessable Entity)
    @ExceptionHandler(BusinessRuleViolationException.class)
    public ProblemDetail handleBusinessRuleViolationException(BusinessRuleViolationException ex) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.UNPROCESSABLE_ENTITY, ex.getMessage());
        problemDetail.setTitle("Violación de Regla de Negocio");
        return problemDetail;
    }

    // 4. Errores de Validación @Valid (400 Bad Request)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ProblemDetail handleValidationExceptions(MethodArgumentNotValidException ex) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, "Error de validación en los campos del formulario.");
        problemDetail.setTitle("Datos de Petición Inválidos");

        // Transformamos los errores de campos en un Mapa (campo -> mensaje)
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });

        // ProblemDetail permite agregar propiedades personalizadas mediante "setProperty"
        problemDetail.setProperty("invalid_fields", errors);
        problemDetail.setProperty("timestamp", Instant.now());

        return problemDetail;
    }

    // 5. Red de seguridad para cualquier otro error no controlado (500 Internal Server Error)
    @ExceptionHandler(Exception.class)
    public ProblemDetail handleGlobalException(Exception ex) {
        // IMPORTANTE: Aquí deberías usar un Logger (ej. log.error("Error no controlado", ex))
        // Nunca le muestres el mensaje original (ex.getMessage()) al usuario si es un 500, por seguridad.

        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(
                HttpStatus.INTERNAL_SERVER_ERROR,
                "Ocurrió un error interno en el servidor. Por favor, contacta al soporte."
        );
        problemDetail.setTitle("Error Interno del Servidor");
        return problemDetail;
    }
}