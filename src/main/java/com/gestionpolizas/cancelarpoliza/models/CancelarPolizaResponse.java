package com.gestionpolizas.cancelarpoliza.models;

import com.gestionpolizas.consultarpolizas.dto.PolizaDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Response del endpoint de cancelación de póliza. Incluye la póliza cancelada y la cantidad de
 * riesgos que fueron cancelados en cascada.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CancelarPolizaResponse {

    private PolizaDto poliza;
    private int riesgosCancelados;
}
