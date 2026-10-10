package com.gestionpolizas.registrareventocore.controller;

import com.gestionpolizas.commons.dto.ApiResponse;
import com.gestionpolizas.registrareventocore.models.CoreEventoRequest;
import com.gestionpolizas.registrareventocore.models.CoreEventoResponse;
import com.gestionpolizas.registrareventocore.service.RegistrarEventoCoreService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Endpoint que registra eventos en el CORE de seguros (mock). Registra en logs que la operacion
 * se intento enviar al sistema legado.
 */
@RestController
@RequestMapping("/api/v1/core-mock")
@RequiredArgsConstructor
@Tag(name = "Registrar Evento Core", description = "Simulacion de la integracion con el CORE de seguros legado")
public class RegistrarEventoCoreController {

    private final RegistrarEventoCoreService registrarEventoCoreService;

    @PostMapping("/evento")
    @Operation(summary = "Registrar evento en el CORE (mock)",
            description = "Registra en logs el intento de envio de un evento al CORE legado.")
    public ResponseEntity<ApiResponse<CoreEventoResponse>> registrarEvento(
            @Valid @RequestBody CoreEventoRequest request) {
        CoreEventoResponse response = registrarEventoCoreService.registrarEvento(request);
        return ResponseEntity.ok(ApiResponse.exito(response));
    }
}
