package com.gestionpolizas.consultarriesgos.service;

import com.gestionpolizas.consultarriesgos.models.ConsultarRiesgosResponse;

/**
 * Servicio de consulta de los riesgos asociados a una póliza.
 */
public interface ConsultarRiesgosService {

    /**
     * Lista los riesgos de la póliza indicada.
     *
     * @param polizaId identificador de la póliza
     * @return respuesta con los riesgos de la póliza
     */
    ConsultarRiesgosResponse consultarPorPoliza(Long polizaId);
}
