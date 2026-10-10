package com.gestionpolizas.consultarpolizas.controller;

import com.gestionpolizas.commons.dto.ApiResponse;
import com.gestionpolizas.consultarpolizas.models.ConsultarPolizasResponse;
import com.gestionpolizas.consultarpolizas.service.ConsultarPolizasService;
import com.gestionpolizas.commons.entity.EstadoPoliza;
import com.gestionpolizas.commons.entity.TipoPoliza;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller de consulta de polizas. Permite listar polizas filtrando por tipo y estado.
 */
@RestController
@RequestMapping("/api/v1/polizas")
@RequiredArgsConstructor
@Tag(name = "Consultar Polizas", description = "Listado de polizas por tipo y estado")
public class ConsultarPolizasController {

    private final ConsultarPolizasService service;

    @GetMapping
    @Operation(summary = "Listar polizas",
            description = "Lista las polizas filtrando opcionalmente por tipo (INDIVIDUAL/COLECTIVA) "
                    + "y estado (ACTIVA/RENOVADA/CANCELADA).")
    public ResponseEntity<ApiResponse<ConsultarPolizasResponse>> consultar(
            @RequestParam(required = false) TipoPoliza tipo,
            @RequestParam(required = false) EstadoPoliza estado) {
        ConsultarPolizasResponse response = service.consultar(tipo, estado);
        return ResponseEntity.ok(ApiResponse.exito(response));
    }
}
