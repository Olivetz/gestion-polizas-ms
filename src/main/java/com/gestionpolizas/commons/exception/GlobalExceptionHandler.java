package com.gestionpolizas.commons.exception;

import java.util.List;

import com.gestionpolizas.commons.dto.ApiResponse;
import com.gestionpolizas.commons.dto.BusinessError;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * Manejador global de excepciones. Centraliza la traducción de excepciones a respuestas HTTP
 * uniformes ({@link ApiResponse}), evitando exponer stack traces o detalles internos al cliente
 * (OWASP A10 / A02) y garantizando un comportamiento fail-safe (nunca fail-open).
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    /** Recurso inexistente → 404. */
    @ExceptionHandler(RecursoNoEncontradoException.class)
    public ResponseEntity<ApiResponse<Void>> manejarNoEncontrado(RecursoNoEncontradoException ex) {
        log.warn("Recurso no encontrado: {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(ApiResponse.error("RECURSO_NO_ENCONTRADO", ex.getMessage()));
    }

    /** Violación de regla de negocio → 409. */
    @ExceptionHandler(ReglaNegocioException.class)
    public ResponseEntity<ApiResponse<Void>> manejarReglaNegocio(ReglaNegocioException ex) {
        log.warn("Regla de negocio violada: {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(ApiResponse.error("REGLA_NEGOCIO", ex.getMessage()));
    }

    /**
     * Error de validación de los datos de entrada (Bean Validation) → 400.
     * Cada campo inválido se devuelve como un error independiente dentro de la lista.
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<Void>> manejarValidacion(MethodArgumentNotValidException ex) {
        List<BusinessError> errores = ex.getBindingResult().getFieldErrors().stream()
                .map(this::aBusinessError)
                .toList();
        log.warn("Error de validación de entrada: {} campo(s) inválido(s)", errores.size());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ApiResponse.errores(errores));
    }

    /**
     * Último recurso para excepciones no previstas → 500. Se registra el detalle internamente
     * pero al cliente solo se le devuelve un mensaje genérico (no se filtra información interna).
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Void>> manejarGeneral(Exception ex) {
        log.error("Error no controlado procesando la solicitud", ex);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiResponse.error("ERROR_INTERNO",
                        "Ocurrió un error procesando la solicitud. Intente nuevamente."));
    }

    /**
     * Convierte un error de campo de Bean Validation en un {@link BusinessError}, usando el nombre
     * del campo como código y el mensaje de la validación como descripción.
     */
    private BusinessError aBusinessError(FieldError error) {
        return BusinessError.builder()
                .codError("VALIDACION")
                .descError(error.getField() + ": " + error.getDefaultMessage())
                .build();
    }
}
