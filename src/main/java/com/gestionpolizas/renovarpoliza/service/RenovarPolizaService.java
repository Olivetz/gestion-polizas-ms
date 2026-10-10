package com.gestionpolizas.renovarpoliza.service;

import com.gestionpolizas.renovarpoliza.models.RenovarPolizaResponse;

/**
 * Servicio de renovación de pólizas.
 */
public interface RenovarPolizaService {

    /**
     * Renueva la póliza indicada: ajusta el canon según el IPC configurado, recalcula la prima,
     * extiende la vigencia por el mismo periodo y deja la póliza en estado RENOVADA.
     *
     * @param polizaId identificador de la póliza a renovar
     * @return detalle de la renovación aplicada
     */
    RenovarPolizaResponse renovar(Long polizaId);
}
