package com.gestionpolizas.renovarpoliza.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;

import com.gestionpolizas.commons.exception.RecursoNoEncontradoException;
import com.gestionpolizas.commons.exception.ReglaNegocioException;
import com.gestionpolizas.consultarpolizas.dto.PolizaDto;
import com.gestionpolizas.registrareventocore.models.CoreEventoRequest;
import com.gestionpolizas.registrareventocore.service.RegistrarEventoCoreService;
import com.gestionpolizas.commons.entity.EstadoPoliza;
import com.gestionpolizas.commons.entity.Poliza;
import com.gestionpolizas.commons.repository.PolizaRepository;
import com.gestionpolizas.renovarpoliza.models.RenovarPolizaResponse;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Implementacion de la renovacion de polizas.
 *
 * <p>Reglas aplicadas:</p>
 * <ul>
 *   <li>No se puede renovar una poliza en estado CANCELADA.</li>
 *   <li>El canon se incrementa segun el IPC configurado: {@code canonNuevo = canon * (1 + IPC/100)}.</li>
 *   <li>La prima se recalcula como {@code canonNuevo * numeroMesesVigencia}.</li>
 *   <li>La vigencia se extiende por el mismo numero de meses del periodo inicial.</li>
 *   <li>El estado pasa a RENOVADA y se notifica el cambio al CORE (mock).</li>
 * </ul>
 */
@Service
@RequiredArgsConstructor
public class RenovarPolizaServiceImpl implements RenovarPolizaService {

    private static final String EVENTO_RENOVACION = "RENOVACION";
    private static final int ESCALA_MONETARIA = 2;

    private final PolizaRepository polizaRepository;
    private final RegistrarEventoCoreService registrarEventoCoreService;

    /** Porcentaje de IPC a aplicar en la renovacion (parametro configurable, no hardcodeado). */
    @Value("${negocio.ipc-porcentaje}")
    private BigDecimal ipcPorcentaje;

    @Override
    @Transactional
    public RenovarPolizaResponse renovar(Long polizaId) {
        Poliza poliza = polizaRepository.findById(polizaId)
                .orElseThrow(() -> new RecursoNoEncontradoException(
                        "No existe la póliza con id " + polizaId));

        if (poliza.getEstado() == EstadoPoliza.CANCELADA) {
            throw new ReglaNegocioException("No se puede renovar una póliza cancelada.");
        }

        BigDecimal canonAnterior = poliza.getCanonMensual();
        BigDecimal primaAnterior = poliza.getPrima();

        // Ajuste del canon segun IPC: canonNuevo = canon * (1 + IPC/100)
        BigDecimal factor = BigDecimal.ONE.add(
                ipcPorcentaje.divide(BigDecimal.valueOf(100), 6, RoundingMode.HALF_UP));
        BigDecimal canonNuevo = canonAnterior.multiply(factor)
                .setScale(ESCALA_MONETARIA, RoundingMode.HALF_UP);

        // Recalcular prima = canonNuevo * numeroMesesVigencia
        BigDecimal primaNueva = canonNuevo
                .multiply(BigDecimal.valueOf(poliza.getNumeroMesesVigencia()))
                .setScale(ESCALA_MONETARIA, RoundingMode.HALF_UP);

        // Extender la vigencia por el mismo periodo inicial
        poliza.setFechaInicioVigencia(poliza.getFechaFinVigencia());
        poliza.setFechaFinVigencia(
                poliza.getFechaFinVigencia().plusMonths(poliza.getNumeroMesesVigencia()));

        poliza.setCanonMensual(canonNuevo);
        poliza.setPrima(primaNueva);
        poliza.setIpcAplicado(ipcPorcentaje);
        poliza.setEstado(EstadoPoliza.RENOVADA);
        poliza.setFechaActualizacion(LocalDateTime.now());

        Poliza guardada = polizaRepository.save(poliza);

        // Notificar al CORE legado (mock): se intento enviar la actualizacion de estado.
        registrarEventoCoreService.registrarEvento(
                CoreEventoRequest.builder()
                        .evento(EVENTO_RENOVACION)
                        .polizaId(guardada.getId())
                        .build());

        return RenovarPolizaResponse.builder()
                .poliza(PolizaDto.desde(guardada))
                .ipcAplicado(ipcPorcentaje)
                .canonAnterior(canonAnterior)
                .canonNuevo(canonNuevo)
                .primaAnterior(primaAnterior)
                .primaNueva(primaNueva)
                .build();
    }
}
