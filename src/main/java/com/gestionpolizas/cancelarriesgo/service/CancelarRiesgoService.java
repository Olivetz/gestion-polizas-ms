package com.gestionpolizas.cancelarriesgo.service;

import com.gestionpolizas.cancelarriesgo.models.CancelarRiesgoResponse;

/**
 * Servicio de cancelación de un riesgo individual.
 */
public interface CancelarRiesgoService {

    /**
     * Cancela el riesgo indicado.
     *
     * @param riesgoId identificador del riesgo a cancelar
     * @return detalle del riesgo cancelado
     */
    CancelarRiesgoResponse cancelar(Long riesgoId);
}
