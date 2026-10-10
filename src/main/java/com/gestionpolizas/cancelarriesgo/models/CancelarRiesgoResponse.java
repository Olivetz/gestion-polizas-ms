package com.gestionpolizas.cancelarriesgo.models;

import com.gestionpolizas.consultarriesgos.dto.RiesgoDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Response del endpoint de cancelación de riesgo. Devuelve el riesgo cancelado.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CancelarRiesgoResponse {

    private RiesgoDto riesgo;
}
