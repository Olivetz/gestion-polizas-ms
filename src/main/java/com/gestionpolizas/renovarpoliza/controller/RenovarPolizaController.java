package com.gestionpolizas.renovarpoliza.controller;

import com.gestionpolizas.commons.dto.ApiResponse;
import com.gestionpolizas.renovarpoliza.models.RenovarPolizaResponse;
import com.gestionpolizas.renovarpoliza.service.RenovarPolizaService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller de renovación de pólizas.
 */
@RestController
@RequestMapping("/api/v1/polizas")
@RequiredArgsConstructor
@Tag(name = "Renovar Póliza", description = "Renovación de pólizas con ajuste de canon por IPC")
public class RenovarPolizaController {

    private final RenovarPolizaService service;

    @PostMapping("/{id}/renovar")
    @Operation(summary = "Renovar póliza",
            description = "Incrementa el canon y la prima según el IPC, extiende la vigencia y deja "
                    + "la póliza en estado RENOVADA. No se permite renovar pólizas canceladas.")
    public ResponseEntity<ApiResponse<RenovarPolizaResponse>> renovar(@PathVariable Long id) {
        RenovarPolizaResponse response = service.renovar(id);
        return ResponseEntity.ok(ApiResponse.exito(response));
    }
}
