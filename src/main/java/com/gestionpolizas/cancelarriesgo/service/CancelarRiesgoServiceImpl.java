package com.gestionpolizas.cancelarriesgo.service;

import java.time.LocalDate;

import com.gestionpolizas.cancelarriesgo.models.CancelarRiesgoResponse;
import com.gestionpolizas.commons.exception.RecursoNoEncontradoException;
import com.gestionpolizas.commons.exception.ReglaNegocioException;
import com.gestionpolizas.consultarriesgos.dto.RiesgoDto;
import com.gestionpolizas.registrareventocore.models.CoreEventoRequest;
import com.gestionpolizas.registrareventocore.service.RegistrarEventoCoreService;
import com.gestionpolizas.commons.entity.EstadoRiesgo;
import com.gestionpolizas.commons.entity.Riesgo;
import com.gestionpolizas.commons.repository.RiesgoRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Implementacion de la cancelacion de un riesgo individual.
 *
 * <p>Reglas aplicadas:</p>
 * <ul>
 *   <li>Si el riesgo ya esta cancelado, se rechaza la operacion.</li>
 *   <li>El cambio se notifica al CORE (mock).</li>
 * </ul>
 */
@Service
@RequiredArgsConstructor
public class CancelarRiesgoServiceImpl implements CancelarRiesgoService {

    private static final String EVENTO_CANCELAR_RIESGO = "CANCELAR_RIESGO";

    private final RiesgoRepository riesgoRepository;
    private final RegistrarEventoCoreService registrarEventoCoreService;

    @Override
    @Transactional
    public CancelarRiesgoResponse cancelar(Long riesgoId) {
        Riesgo riesgo = riesgoRepository.findById(riesgoId)
                .orElseThrow(() -> new RecursoNoEncontradoException(
                        "No existe el riesgo con id " + riesgoId));

        if (riesgo.getEstado() == EstadoRiesgo.CANCELADO) {
            throw new ReglaNegocioException("El riesgo ya se encuentra cancelado.");
        }

        riesgo.setEstado(EstadoRiesgo.CANCELADO);
        riesgo.setFechaCancelacion(LocalDate.now());

        Riesgo guardado = riesgoRepository.save(riesgo);

        Long polizaId = guardado.getPoliza() != null ? guardado.getPoliza().getId() : null;
        registrarEventoCoreService.registrarEvento(
                CoreEventoRequest.builder()
                        .evento(EVENTO_CANCELAR_RIESGO)
                        .polizaId(polizaId)
                        .build());

        return CancelarRiesgoResponse.builder()
                .riesgo(RiesgoDto.desde(guardado))
                .build();
    }
}
