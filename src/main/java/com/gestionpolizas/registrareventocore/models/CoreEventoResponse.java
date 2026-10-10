package com.gestionpolizas.registrareventocore.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Response del endpoint que registra un evento en el CORE. Confirma que el evento fue recibido
 * y registrado.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CoreEventoResponse {

    private boolean recibido;
    private String evento;
    private Long polizaId;
    private String mensaje;
}
