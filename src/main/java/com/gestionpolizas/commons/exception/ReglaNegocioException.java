package com.gestionpolizas.commons.exception;

/**
 * Se lanza cuando una operación viola una regla de negocio del dominio
 * (p. ej. agregar un segundo riesgo a una póliza individual, o renovar una póliza cancelada).
 * Se traduce a un HTTP 409 (Conflict) en el manejador global de excepciones.
 */
public class ReglaNegocioException extends RuntimeException {

    public ReglaNegocioException(String mensaje) {
        super(mensaje);
    }
}
