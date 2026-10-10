package com.gestionpolizas.consultarpolizas.service;

import com.gestionpolizas.consultarpolizas.models.ConsultarPolizasResponse;
import com.gestionpolizas.commons.entity.EstadoPoliza;
import com.gestionpolizas.commons.entity.TipoPoliza;

/**
 * Servicio de consulta de polizas con filtros opcionales por tipo y estado.
 */
public interface ConsultarPolizasService {

    /**
     * Lista las polizas aplicando los filtros indicados. Ambos filtros son opcionales:
     * si alguno es {@code null}, no se aplica.
     *
     * @param tipo   tipo de poliza a filtrar (opcional)
     * @param estado estado de poliza a filtrar (opcional)
     * @return respuesta con las polizas encontradas y el total
     */
    ConsultarPolizasResponse consultar(TipoPoliza tipo, EstadoPoliza estado);
}
