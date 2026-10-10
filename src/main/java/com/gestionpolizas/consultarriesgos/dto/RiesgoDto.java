package com.gestionpolizas.consultarriesgos.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

import com.gestionpolizas.commons.entity.EstadoRiesgo;
import com.gestionpolizas.commons.entity.Riesgo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO de salida con los datos de un riesgo. Evita exponer la entidad JPA directamente.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RiesgoDto {

    private Long id;
    private Long polizaId;
    private EstadoRiesgo estado;
    private String inmuebleDireccion;
    private BigDecimal canonAsociado;
    private LocalDate fechaCancelacion;

    /**
     * Construye un DTO a partir de la entidad de dominio.
     *
     * @param riesgo entidad origen
     * @return DTO con los campos publicos del riesgo
     */
    public static RiesgoDto desde(Riesgo riesgo) {
        return RiesgoDto.builder()
                .id(riesgo.getId())
                .polizaId(riesgo.getPoliza() != null ? riesgo.getPoliza().getId() : null)
                .estado(riesgo.getEstado())
                .inmuebleDireccion(riesgo.getInmuebleDireccion())
                .canonAsociado(riesgo.getCanonAsociado())
                .fechaCancelacion(riesgo.getFechaCancelacion())
                .build();
    }
}
