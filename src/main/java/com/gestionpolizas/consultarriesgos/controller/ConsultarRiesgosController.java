package com.gestionpolizas.consultarriesgos.controller;

import com.gestionpolizas.commons.dto.ApiResponse;
import com.gestionpolizas.consultarriesgos.models.ConsultarRiesgosResponse;
import com.gestionpolizas.consultarriesgos.service.ConsultarRiesgosService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller de consulta de riesgos de una póliza.
 */
@RestController
@RequestMapping("/api/v1/polizas")
@RequiredArgsConstructor
@Tag(name = "Consultar Riesgos", description = "Listado de riesgos asociados a una póliza")
public class ConsultarRiesgosController {

    private final ConsultarRiesgosService service;

    @GetMapping("/{id}/riesgos")
    @Operation(summary = "Listar riesgos de una póliza",
            description = "Devuelve los riesgos asociados a la póliza indicada.")
    public ResponseEntity<ApiResponse<ConsultarRiesgosResponse>> consultar(@PathVariable Long id) {
        ConsultarRiesgosResponse response = service.consultarPorPoliza(id);
        return ResponseEntity.ok(ApiResponse.exito(response));
    }
}
