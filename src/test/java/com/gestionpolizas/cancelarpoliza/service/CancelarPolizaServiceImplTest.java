package com.gestionpolizas.cancelarpoliza.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import com.gestionpolizas.cancelarpoliza.models.CancelarPolizaResponse;
import com.gestionpolizas.commons.exception.ReglaNegocioException;
import com.gestionpolizas.registrareventocore.models.CoreEventoRequest;
import com.gestionpolizas.registrareventocore.service.RegistrarEventoCoreService;
import com.gestionpolizas.commons.entity.EstadoPoliza;
import com.gestionpolizas.commons.entity.EstadoRiesgo;
import com.gestionpolizas.commons.entity.Poliza;
import com.gestionpolizas.commons.entity.Riesgo;
import com.gestionpolizas.commons.entity.TipoPoliza;
import com.gestionpolizas.commons.repository.PolizaRepository;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Pruebas unitarias de la logica de cancelacion de polizas (incluida la cascada a riesgos).
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("Cancelar poliza - reglas de negocio")
class CancelarPolizaServiceImplTest {

    @Mock
    private PolizaRepository polizaRepository;

    @Mock
    private RegistrarEventoCoreService registrarEventoCoreService;

    @InjectMocks
    private CancelarPolizaServiceImpl service;

    private Poliza polizaConRiesgos(EstadoPoliza estado) {
        Poliza poliza = Poliza.builder()
                .id(1L)
                .tipo(TipoPoliza.COLECTIVA)
                .estado(estado)
                .fechaInicioVigencia(LocalDate.of(2025, 1, 1))
                .fechaFinVigencia(LocalDate.of(2025, 12, 31))
                .canonMensual(new BigDecimal("1000000.00"))
                .numeroMesesVigencia(12)
                .prima(new BigDecimal("12000000.00"))
                .fechaCreacion(LocalDateTime.now())
                .build();

        Riesgo r1 = Riesgo.builder().id(10L).poliza(poliza).estado(EstadoRiesgo.ACTIVO)
                .inmuebleDireccion("Dir 1").canonAsociado(new BigDecimal("1000000"))
                .fechaCreacion(LocalDateTime.now()).build();
        Riesgo r2 = Riesgo.builder().id(11L).poliza(poliza).estado(EstadoRiesgo.ACTIVO)
                .inmuebleDireccion("Dir 2").canonAsociado(new BigDecimal("1200000"))
                .fechaCreacion(LocalDateTime.now()).build();
        poliza.setRiesgos(List.of(r1, r2));
        return poliza;
    }

    @Test
    @DisplayName("cancela la poliza y en cascada todos sus riesgos activos")
    void cancelaEnCascada() {
        Poliza poliza = polizaConRiesgos(EstadoPoliza.ACTIVA);
        when(polizaRepository.findById(1L)).thenReturn(Optional.of(poliza));
        when(polizaRepository.save(any(Poliza.class))).thenAnswer(inv -> inv.getArgument(0));

        CancelarPolizaResponse response = service.cancelar(1L);

        assertThat(response.getPoliza().getEstado()).isEqualTo(EstadoPoliza.CANCELADA);
        assertThat(response.getRiesgosCancelados()).isEqualTo(2);
        assertThat(poliza.getRiesgos())
                .allMatch(r -> r.getEstado() == EstadoRiesgo.CANCELADO);
        verify(registrarEventoCoreService).registrarEvento(any(CoreEventoRequest.class));
    }

    @Test
    @DisplayName("rechaza cancelar una poliza que ya esta cancelada")
    void rechazaCancelarYaCancelada() {
        Poliza poliza = polizaConRiesgos(EstadoPoliza.CANCELADA);
        when(polizaRepository.findById(1L)).thenReturn(Optional.of(poliza));

        assertThatThrownBy(() -> service.cancelar(1L))
                .isInstanceOf(ReglaNegocioException.class)
                .hasMessageContaining("ya se encuentra cancelada");

        verify(polizaRepository, never()).save(any());
    }
}
