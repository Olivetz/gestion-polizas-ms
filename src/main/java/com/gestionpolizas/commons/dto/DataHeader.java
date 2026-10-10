package com.gestionpolizas.commons.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Cabecera de respuesta estándar. Indica el resultado de la operación mediante un código y,
 * en caso de error, el listado de errores de negocio.
 *
 * <ul>
 *   <li>{@code codRespuesta = 0} indica operación exitosa.</li>
 *   <li>{@code codRespuesta = -1} indica error.</li>
 * </ul>
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DataHeader {

    private int codRespuesta;
    private List<BusinessError> errores;
}
