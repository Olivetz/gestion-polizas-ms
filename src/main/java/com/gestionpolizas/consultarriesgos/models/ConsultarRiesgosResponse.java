package com.gestionpolizas.consultarriesgos.models;

import java.util.List;

import com.gestionpolizas.consultarriesgos.dto.RiesgoDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Response del endpoint de consulta de riesgos de una póliza.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ConsultarRiesgosResponse {

    private Long polizaId;
    private int total;
    private List<RiesgoDto> riesgos;
}
