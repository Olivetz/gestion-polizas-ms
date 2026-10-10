package com.gestionpolizas.cancelarriesgo.controller;

import com.gestionpolizas.cancelarriesgo.models.CancelarRiesgoResponse;
import com.gestionpolizas.cancelarriesgo.service.CancelarRiesgoService;
import com.gestionpolizas.commons.dto.ApiResponse;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller de cancelación de un riesgo individual.
 */
@RestController
@RequestMapping("/api/v1/riesgos")
@RequiredArgsConstructor
@Tag(name = "Cancelar Riesgo", description = "Cancelación de un riesgo individual")
public class CancelarRiesgoController {

    private final CancelarRiesgoService service;

    @PostMapping("/{id}/cancelar")
    @Operation(summary = "Cancelar riesgo",
            description = "Cancela el riesgo indicado por su identificador.")
    public ResponseEntity<ApiResponse<CancelarRiesgoResponse>> cancelar(@PathVariable Long id) {
        CancelarRiesgoResponse response = service.cancelar(id);
        return ResponseEntity.ok(ApiResponse.exito(response));
    }
}
