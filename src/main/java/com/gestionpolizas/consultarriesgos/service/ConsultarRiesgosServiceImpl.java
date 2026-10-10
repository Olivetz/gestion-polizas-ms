package com.gestionpolizas.consultarriesgos.service;

import java.util.List;

import com.gestionpolizas.commons.exception.RecursoNoEncontradoException;
import com.gestionpolizas.consultarriesgos.dto.RiesgoDto;
import com.gestionpolizas.consultarriesgos.models.ConsultarRiesgosResponse;
import com.gestionpolizas.commons.repository.PolizaRepository;
import com.gestionpolizas.commons.repository.RiesgoRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Implementacion de la consulta de riesgos por poliza. Verifica primero que la poliza exista
 * para devolver un 404 claro en lugar de una lista vacia ambigua.
 */
@Service
@RequiredArgsConstructor
public class ConsultarRiesgosServiceImpl implements ConsultarRiesgosService {

    private final RiesgoRepository riesgoRepository;
    private final PolizaRepository polizaRepository;

    @Override
    @Transactional(readOnly = true)
    public ConsultarRiesgosResponse consultarPorPoliza(Long polizaId) {
        if (!polizaRepository.existsById(polizaId)) {
            throw new RecursoNoEncontradoException("No existe la póliza con id " + polizaId);
        }

        List<RiesgoDto> riesgos = riesgoRepository.findByPolizaId(polizaId).stream()
                .map(RiesgoDto::desde)
                .toList();

        return ConsultarRiesgosResponse.builder()
                .polizaId(polizaId)
                .total(riesgos.size())
                .riesgos(riesgos)
                .build();
    }
}
