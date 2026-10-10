package com.gestionpolizas.agregarriesgo.controller;

import com.gestionpolizas.agregarriesgo.models.AgregarRiesgoRequest;
import com.gestionpolizas.agregarriesgo.models.AgregarRiesgoResponse;
import com.gestionpolizas.agregarriesgo.service.AgregarRiesgoService;
import com.gestionpolizas.commons.dto.ApiResponse;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller para agregar riesgos a una póliza.
 */
@RestController
@RequestMapping("/api/v1/polizas")
@RequiredArgsConstructor
@Tag(name = "Agregar Riesgo", description = "Adición de riesgos a una póliza según su tipo")
public class AgregarRiesgoController {

    private final AgregarRiesgoService service;

    @PostMapping("/{id}/riesgos")
    @Operation(summary = "Agregar riesgo a una póliza",
            description = "Agrega un riesgo validando el tipo de póliza. Una póliza individual admite "
                    + "máximo un riesgo; una colectiva admite varios.")
    public ResponseEntity<ApiResponse<AgregarRiesgoResponse>> agregar(
            @PathVariable Long id,
            @Valid @RequestBody AgregarRiesgoRequest request) {
        AgregarRiesgoResponse response = service.agregar(id, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.exito(response));
    }
}
