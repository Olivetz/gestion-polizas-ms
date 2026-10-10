package com.gestionpolizas.agregarriesgo.models;

import com.gestionpolizas.consultarriesgos.dto.RiesgoDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Response del endpoint de agregar riesgo. Devuelve el riesgo creado.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AgregarRiesgoResponse {

    private RiesgoDto riesgo;
}
