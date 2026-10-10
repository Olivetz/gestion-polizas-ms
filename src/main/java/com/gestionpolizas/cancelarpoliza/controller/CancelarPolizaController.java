package com.gestionpolizas.cancelarpoliza.controller;

import com.gestionpolizas.cancelarpoliza.models.CancelarPolizaResponse;
import com.gestionpolizas.cancelarpoliza.service.CancelarPolizaService;
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
 * Controller de cancelación de pólizas.
 */
@RestController
@RequestMapping("/api/v1/polizas")
@RequiredArgsConstructor
@Tag(name = "Cancelar Póliza", description = "Cancelación de pólizas con cascada a sus riesgos")
public class CancelarPolizaController {

    private final CancelarPolizaService service;

    @PostMapping("/{id}/cancelar")
    @Operation(summary = "Cancelar póliza",
            description = "Cancela la póliza y, en cascada, todos sus riesgos activos.")
    public ResponseEntity<ApiResponse<CancelarPolizaResponse>> cancelar(@PathVariable Long id) {
        CancelarPolizaResponse response = service.cancelar(id);
        return ResponseEntity.ok(ApiResponse.exito(response));
    }
}
