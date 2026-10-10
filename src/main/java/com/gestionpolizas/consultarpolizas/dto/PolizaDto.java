package com.gestionpolizas.consultarpolizas.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

import com.gestionpolizas.commons.entity.EstadoPoliza;
import com.gestionpolizas.commons.entity.Poliza;
import com.gestionpolizas.commons.entity.TipoPoliza;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO de salida con los datos de una poliza. Expone unicamente los campos necesarios para el
 * cliente, evitando exponer directamente la entidad JPA (OWASP A01/A09 - allowlist de campos).
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PolizaDto {

    private Long id;
    private TipoPoliza tipo;
    private EstadoPoliza estado;
    private LocalDate fechaInicioVigencia;
    private LocalDate fechaFinVigencia;
    private BigDecimal canonMensual;
    private Integer numeroMesesVigencia;
    private BigDecimal prima;
    private BigDecimal ipcAplicado;

    /**
     * Construye un DTO a partir de la entidad de dominio.
     *
     * @param poliza entidad origen
     * @return DTO con los campos publicos de la poliza
     */
    public static PolizaDto desde(Poliza poliza) {
        return PolizaDto.builder()
                .id(poliza.getId())
                .tipo(poliza.getTipo())
                .estado(poliza.getEstado())
                .fechaInicioVigencia(poliza.getFechaInicioVigencia())
                .fechaFinVigencia(poliza.getFechaFinVigencia())
                .canonMensual(poliza.getCanonMensual())
                .numeroMesesVigencia(poliza.getNumeroMesesVigencia())
                .prima(poliza.getPrima())
                .ipcAplicado(poliza.getIpcAplicado())
                .build();
    }
}
