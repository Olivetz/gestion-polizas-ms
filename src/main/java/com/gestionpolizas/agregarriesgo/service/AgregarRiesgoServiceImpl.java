package com.gestionpolizas.agregarriesgo.service;

import java.time.LocalDateTime;

import com.gestionpolizas.agregarriesgo.models.AgregarRiesgoRequest;
import com.gestionpolizas.agregarriesgo.models.AgregarRiesgoResponse;
import com.gestionpolizas.commons.exception.RecursoNoEncontradoException;
import com.gestionpolizas.commons.exception.ReglaNegocioException;
import com.gestionpolizas.consultarriesgos.dto.RiesgoDto;
import com.gestionpolizas.registrareventocore.models.CoreEventoRequest;
import com.gestionpolizas.registrareventocore.service.RegistrarEventoCoreService;
import com.gestionpolizas.commons.entity.EstadoPoliza;
import com.gestionpolizas.commons.entity.EstadoRiesgo;
import com.gestionpolizas.commons.entity.Poliza;
import com.gestionpolizas.commons.entity.Riesgo;
import com.gestionpolizas.commons.entity.TipoPoliza;
import com.gestionpolizas.commons.repository.PolizaRepository;
import com.gestionpolizas.commons.repository.RiesgoRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Implementacion de la adicion de riesgos a una poliza.
 *
 * <p>Reglas aplicadas:</p>
 * <ul>
 *   <li>No se pueden agregar riesgos a una poliza cancelada.</li>
 *   <li>Solo las polizas de tipo COLECTIVA admiten agregar riesgos por este endpoint
 *       (segun el requerimiento "Solo si tipo = Colectiva"). El riesgo unico de una poliza
 *       individual se asigna en su flujo de creacion, fuera del alcance de esta operacion.</li>
 *   <li>El cambio se notifica al CORE (mock).</li>
 * </ul>
 */
@Service
@RequiredArgsConstructor
public class AgregarRiesgoServiceImpl implements AgregarRiesgoService {

    private static final String EVENTO_AGREGAR_RIESGO = "AGREGAR_RIESGO";

    private final PolizaRepository polizaRepository;
    private final RiesgoRepository riesgoRepository;
    private final RegistrarEventoCoreService registrarEventoCoreService;

    @Override
    @Transactional
    public AgregarRiesgoResponse agregar(Long polizaId, AgregarRiesgoRequest request) {
        Poliza poliza = polizaRepository.findById(polizaId)
                .orElseThrow(() -> new RecursoNoEncontradoException(
                        "No existe la póliza con id " + polizaId));

        if (poliza.getEstado() == EstadoPoliza.CANCELADA) {
            throw new ReglaNegocioException("No se pueden agregar riesgos a una póliza cancelada.");
        }

        validarTipoPoliza(poliza);

        Riesgo riesgo = Riesgo.builder()
                .poliza(poliza)
                .estado(EstadoRiesgo.ACTIVO)
                .inmuebleDireccion(request.getInmuebleDireccion().trim())
                .canonAsociado(request.getCanonAsociado())
                .fechaCreacion(LocalDateTime.now())
                .build();

        Riesgo guardado = riesgoRepository.save(riesgo);

        registrarEventoCoreService.registrarEvento(
                CoreEventoRequest.builder()
                        .evento(EVENTO_AGREGAR_RIESGO)
                        .polizaId(poliza.getId())
                        .build());

        return AgregarRiesgoResponse.builder()
                .riesgo(RiesgoDto.desde(guardado))
                .build();
    }

    /**
     * Valida el tipo de poliza. Segun el requerimiento, este endpoint solo admite polizas de
     * tipo COLECTIVA; las individuales se rechazan.
     */
    private void validarTipoPoliza(Poliza poliza) {
        if (poliza.getTipo() != TipoPoliza.COLECTIVA) {
            throw new ReglaNegocioException(
                    "Solo las pólizas colectivas admiten agregar riesgos.");
        }
    }
}
