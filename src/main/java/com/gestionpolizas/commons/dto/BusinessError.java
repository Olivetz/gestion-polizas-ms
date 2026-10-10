package com.gestionpolizas.commons.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Representa un error de negocio dentro de la cabecera de respuesta ({@link DataHeader}).
 * Expone un código y una descripción genérica, sin información interna del sistema.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BusinessError {

    private String codError;
    private String descError;
}
