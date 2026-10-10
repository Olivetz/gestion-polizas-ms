package com.gestionpolizas.renovarpoliza.models;

import java.math.BigDecimal;

import com.gestionpolizas.consultarpolizas.dto.PolizaDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Response del endpoint de renovación. Devuelve la póliza renovada junto con el detalle del
 * ajuste aplicado (IPC, canon anterior y nuevo) para dejar trazabilidad del cálculo.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RenovarPolizaResponse {

    private PolizaDto poliza;
    private BigDecimal ipcAplicado;
    private BigDecimal canonAnterior;
    private BigDecimal canonNuevo;
    private BigDecimal primaAnterior;
    private BigDecimal primaNueva;
}
