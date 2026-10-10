package com.gestionpolizas.commons.entity;

/**
 * Estado del ciclo de vida de una póliza.
 *
 * <ul>
 *   <li>{@link #ACTIVA}: estado inicial; la póliza está vigente.</li>
 *   <li>{@link #RENOVADA}: la póliza fue renovada ajustando el canon por IPC.</li>
 *   <li>{@link #CANCELADA}: estado terminal; no puede renovarse.</li>
 * </ul>
 */
public enum EstadoPoliza {
    ACTIVA,
    RENOVADA,
    CANCELADA
}
