package com.gestionpolizas.consultarpolizas.service;

import java.util.List;

import com.gestionpolizas.consultarpolizas.dto.PolizaDto;
import com.gestionpolizas.consultarpolizas.models.ConsultarPolizasResponse;
import com.gestionpolizas.commons.entity.EstadoPoliza;
import com.gestionpolizas.commons.entity.Poliza;
import com.gestionpolizas.commons.entity.TipoPoliza;
import com.gestionpolizas.commons.repository.PolizaRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Implementacion de la consulta de polizas. Selecciona la consulta adecuada segun los filtros
 * presentes y mapea las entidades a DTOs antes de devolverlas.
 */
@Service
@RequiredArgsConstructor
public class ConsultarPolizasServiceImpl implements ConsultarPolizasService {

    private final PolizaRepository polizaRepository;

    @Override
    @Transactional(readOnly = true)
    public ConsultarPolizasResponse consultar(TipoPoliza tipo, EstadoPoliza estado) {
        List<Poliza> polizas = buscarConFiltros(tipo, estado);

        List<PolizaDto> dtos = polizas.stream()
                .map(PolizaDto::desde)
                .toList();

        return ConsultarPolizasResponse.builder()
                .total(dtos.size())
                .polizas(dtos)
                .build();
    }

    private List<Poliza> buscarConFiltros(TipoPoliza tipo, EstadoPoliza estado) {
        if (tipo != null && estado != null) {
            return polizaRepository.findByTipoAndEstado(tipo, estado);
        }
        if (tipo != null) {
            return polizaRepository.findByTipo(tipo);
        }
        if (estado != null) {
            return polizaRepository.findByEstado(estado);
        }
        return polizaRepository.findAll();
    }
}
