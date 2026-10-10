package com.gestionpolizas.commons.exception;

/**
 * Se lanza cuando un recurso solicitado (póliza o riesgo) no existe.
 * Se traduce a un HTTP 404 en el manejador global de excepciones.
 */
public class RecursoNoEncontradoException extends RuntimeException {

    public RecursoNoEncontradoException(String mensaje) {
        super(mensaje);
    }
}
