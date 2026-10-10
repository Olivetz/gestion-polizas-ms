package com.gestionpolizas.cancelarpoliza.service;

import java.time.LocalDate;
import java.time.LocalDateTime;

import com.gestionpolizas.cancelarpoliza.models.CancelarPolizaResponse;
import com.gestionpolizas.commons.exception.RecursoNoEncontradoException;
import com.gestionpolizas.commons.exception.ReglaNegocioException;
import com.gestionpolizas.consultarpolizas.dto.PolizaDto;
import com.gestionpolizas.registrareventocore.models.CoreEventoRequest;
import com.gestionpolizas.registrareventocore.service.RegistrarEventoCoreService;
import com.gestionpolizas.commons.entity.EstadoPoliza;
import com.gestionpolizas.commons.entity.EstadoRiesgo;
import com.gestionpolizas.commons.entity.Poliza;
import com.gestionpolizas.commons.entity.Riesgo;
import com.gestionpolizas.commons.repository.PolizaRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Implementacion de la cancelacion de polizas.
 *
 * <p>Reglas aplicadas:</p>
 * <ul>
 *   <li>Si la poliza ya esta cancelada, se rechaza la operacion (idempotencia explicita).</li>
 *   <li>Al cancelar la poliza se cancelan en cascada todos sus riesgos activos.</li>
 *   <li>El cambio de estado se notifica al CORE (mock).</li>
 * </ul>
 */
@Service
@RequiredArgsConstructor
public class CancelarPolizaServiceImpl implements CancelarPolizaService {

    private static final String EVENTO_CANCELACION = "CANCELACION";

    private final PolizaRepository polizaRepository;
    private final RegistrarEventoCoreService registrarEventoCoreService;

    @Override
    @Transactional
    public CancelarPolizaResponse cancelar(Long polizaId) {
        Poliza poliza = polizaRepository.findById(polizaId)
                .orElseThrow(() -> new RecursoNoEncontradoException(
                        "No existe la póliza con id " + polizaId));

        if (poliza.getEstado() == EstadoPoliza.CANCELADA) {
            throw new ReglaNegocioException("La póliza ya se encuentra cancelada.");
        }

        LocalDate hoy = LocalDate.now();
        int riesgosCancelados = 0;

        // Cancelacion en cascada de los riesgos activos.
        for (Riesgo riesgo : poliza.getRiesgos()) {
            if (riesgo.getEstado() == EstadoRiesgo.ACTIVO) {
                riesgo.setEstado(EstadoRiesgo.CANCELADO);
                riesgo.setFechaCancelacion(hoy);
                riesgosCancelados++;
            }
        }

        poliza.setEstado(EstadoPoliza.CANCELADA);
        poliza.setFechaActualizacion(LocalDateTime.now());

        Poliza guardada = polizaRepository.save(poliza);

        registrarEventoCoreService.registrarEvento(
                CoreEventoRequest.builder()
                        .evento(EVENTO_CANCELACION)
                        .polizaId(guardada.getId())
                        .build());

        return CancelarPolizaResponse.builder()
                .poliza(PolizaDto.desde(guardada))
                .riesgosCancelados(riesgosCancelados)
                .build();
    }
}
