package com.gestionpolizas.agregarriesgo.models;

import java.math.BigDecimal;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Request para agregar un riesgo a una póliza. Incluye validaciones de entrada en servidor
 * (OWASP A05/A06): obligatoriedad, longitud y rango del canon.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AgregarRiesgoRequest {

    @Schema(description = "Dirección o identificación del inmueble asegurado",
            example = "Calle 100 # 15-20 Apto 301, Bogotá")
    @NotBlank(message = "La dirección del inmueble es obligatoria")
    @Size(max = 300, message = "La dirección no puede superar 300 caracteres")
    private String inmuebleDireccion;

    @Schema(description = "Canon mensual asociado al inmueble", example = "1800000")
    @NotNull(message = "El canon asociado es obligatorio")
    @DecimalMin(value = "0.0", inclusive = false, message = "El canon asociado debe ser mayor a 0")
    private BigDecimal canonAsociado;
}
