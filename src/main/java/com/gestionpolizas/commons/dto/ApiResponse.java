package com.gestionpolizas.commons.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Respuesta genérica envolvente para todas las APIs del microservicio.
 *
 * <p>Estructura uniforme basada en el estándar corporativo:</p>
 * <pre>
 * {
 *   "dataHeader": { "codRespuesta": 0, "errores": [...] },
 *   "data": { ... }
 * }
 * </pre>
 *
 * @param <T> tipo de datos contenidos en {@code data}
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.ALWAYS)
public class ApiResponse<T> {

    /** Código de operación exitosa. */
    private static final int COD_EXITO = 0;
    /** Código de operación con error. */
    private static final int COD_ERROR = -1;

    private DataHeader dataHeader;
    private T data;

    /**
     * Crea una respuesta exitosa (codRespuesta = 0) con los datos proporcionados.
     *
     * @param data datos de la respuesta
     * @param <T>  tipo de datos
     * @return ApiResponse con cabecera de éxito
     */
    public static <T> ApiResponse<T> exito(T data) {
        DataHeader header = DataHeader.builder()
                .codRespuesta(COD_EXITO)
                .errores(List.of())
                .build();
        return new ApiResponse<>(header, data);
    }

    /**
     * Crea una respuesta de error (codRespuesta = -1) con un código y mensaje de negocio.
     *
     * @param codError código identificador del error
     * @param descError descripción del error (genérica, sin información interna)
     * @param <T>      tipo de datos (será null en la respuesta)
     * @return ApiResponse con cabecera de error
     */
    public static <T> ApiResponse<T> error(String codError, String descError) {
        BusinessError businessError = BusinessError.builder()
                .codError(codError)
                .descError(descError)
                .build();
        return errores(List.of(businessError));
    }

    /**
     * Crea una respuesta de error (codRespuesta = -1) con una lista de errores de negocio.
     * Útil cuando una validación produce varios errores a la vez.
     *
     * @param errores lista de errores a devolver en la cabecera
     * @param <T>     tipo de datos (será null en la respuesta)
     * @return ApiResponse con cabecera de error
     */
    public static <T> ApiResponse<T> errores(List<BusinessError> errores) {
        DataHeader header = DataHeader.builder()
                .codRespuesta(COD_ERROR)
                .errores(errores)
                .build();
        return new ApiResponse<>(header, null);
    }
}
