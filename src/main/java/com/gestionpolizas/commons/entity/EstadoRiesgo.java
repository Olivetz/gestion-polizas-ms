package com.gestionpolizas.commons.entity;

/**
 * Estado del ciclo de vida de un riesgo (inmueble asegurado).
 *
 * <ul>
 *   <li>{@link #ACTIVO}: el riesgo está vigente dentro de la póliza.</li>
 *   <li>{@link #CANCELADO}: el riesgo fue cancelado (individualmente o por cascada al cancelar la póliza).</li>
 * </ul>
 */
public enum EstadoRiesgo {
    ACTIVO,
    CANCELADO
}
