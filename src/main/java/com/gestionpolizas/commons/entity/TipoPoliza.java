package com.gestionpolizas.commons.entity;

/**
 * Tipo de póliza según el dominio de negocio.
 *
 * <ul>
 *   <li>{@link #INDIVIDUAL}: el tomador y asegurado es el arrendatario; admite exactamente 1 riesgo.</li>
 *   <li>{@link #COLECTIVA}: orientada a inmobiliarias/administraciones; admite 1..N riesgos.</li>
 * </ul>
 */
public enum TipoPoliza {
    INDIVIDUAL,
    COLECTIVA
}
