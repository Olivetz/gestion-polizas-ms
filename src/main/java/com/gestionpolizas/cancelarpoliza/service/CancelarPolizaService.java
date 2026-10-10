package com.gestionpolizas.cancelarpoliza.service;

import com.gestionpolizas.cancelarpoliza.models.CancelarPolizaResponse;

/**
 * Servicio de cancelación de pólizas.
 */
public interface CancelarPolizaService {

    /**
     * Cancela la póliza indicada y, en cascada, todos sus riesgos activos.
     *
     * @param polizaId identificador de la póliza a cancelar
     * @return detalle de la cancelación (póliza y número de riesgos cancelados)
     */
    CancelarPolizaResponse cancelar(Long polizaId);
}
