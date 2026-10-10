package com.gestionpolizas.consultarpolizas.models;

import java.util.List;

import com.gestionpolizas.consultarpolizas.dto.PolizaDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Response del endpoint de consulta de pólizas. Contiene la lista de pólizas que cumplen el
 * filtro y la cantidad total encontrada.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ConsultarPolizasResponse {

    private int total;
    private List<PolizaDto> polizas;
}
