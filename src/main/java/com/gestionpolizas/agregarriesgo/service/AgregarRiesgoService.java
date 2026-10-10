package com.gestionpolizas.agregarriesgo.service;

import com.gestionpolizas.agregarriesgo.models.AgregarRiesgoRequest;
import com.gestionpolizas.agregarriesgo.models.AgregarRiesgoResponse;

/**
 * Servicio para agregar riesgos a una póliza.
 */
public interface AgregarRiesgoService {

    /**
     * Agrega un riesgo a la póliza indicada, validando las reglas de negocio según el tipo de
     * póliza (individual admite 1 riesgo; colectiva admite varios).
     *
     * @param polizaId identificador de la póliza
     * @param request  datos del riesgo a agregar
     * @return el riesgo creado
     */
    AgregarRiesgoResponse agregar(Long polizaId, AgregarRiesgoRequest request);
}
