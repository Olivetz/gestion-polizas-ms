package com.gestionpolizas.registrareventocore.models;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Request del endpoint que registra un evento en el CORE. Representa el evento que se
 * "intentaria enviar" al sistema CORE transaccional legado a traves del servicio agnostico
 * de edicion.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CoreEventoRequest {

    @Schema(description = "Tipo de evento notificado al CORE", example = "ACTUALIZACION")
    @NotBlank(message = "El evento es obligatorio")
    private String evento;

    @Schema(description = "Identificador de la poliza afectada", example = "555")
    @NotNull(message = "El polizaId es obligatorio")
    private Long polizaId;
}
