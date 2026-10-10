package com.gestionpolizas.renovarpoliza.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

import com.gestionpolizas.commons.exception.RecursoNoEncontradoException;
import com.gestionpolizas.commons.exception.ReglaNegocioException;
import com.gestionpolizas.registrareventocore.models.CoreEventoRequest;
import com.gestionpolizas.registrareventocore.service.RegistrarEventoCoreService;
import com.gestionpolizas.commons.entity.EstadoPoliza;
import com.gestionpolizas.commons.entity.Poliza;
import com.gestionpolizas.commons.entity.TipoPoliza;
import com.gestionpolizas.commons.repository.PolizaRepository;
import com.gestionpolizas.renovarpoliza.models.RenovarPolizaResponse;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Pruebas unitarias de la logica de renovacion de polizas.
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("Renovar poliza - reglas de negocio")
class RenovarPolizaServiceImplTest {

    @Mock
    private PolizaRepository polizaRepository;

    @Mock
    private RegistrarEventoCoreService registrarEventoCoreService;

    @InjectMocks
    private RenovarPolizaServiceImpl service;

    @BeforeEach
    void configurarIpc() {
        // IPC de prueba: 5%. Se inyecta directamente el campo @Value.
        ReflectionTestUtils.setField(service, "ipcPorcentaje", new BigDecimal("5.0"));
    }

    private Poliza polizaActiva() {
        return Poliza.builder()
                .id(1L)
                .tipo(TipoPoliza.INDIVIDUAL)
                .estado(EstadoPoliza.ACTIVA)
                .fechaInicioVigencia(LocalDate.of(2025, 1, 1))
                .fechaFinVigencia(LocalDate.of(2025, 12, 31))
                .canonMensual(new BigDecimal("1000000.00"))
                .numeroMesesVigencia(12)
                .prima(new BigDecimal("12000000.00"))
                .fechaCreacion(LocalDateTime.now())
                .build();
    }

    @Test
    @DisplayName("renueva incrementando canon y prima segun el IPC y deja estado RENOVADA")
    void renuevaAplicandoIpc() {
        Poliza poliza = polizaActiva();
        when(polizaRepository.findById(1L)).thenReturn(Optional.of(poliza));
        when(polizaRepository.save(any(Poliza.class))).thenAnswer(inv -> inv.getArgument(0));

        RenovarPolizaResponse response = service.renovar(1L);

        // canon 1.000.000 * 1.05 = 1.050.000 ; prima = 1.050.000 * 12 = 12.600.000
        assertThat(response.getCanonNuevo()).isEqualByComparingTo("1050000.00");
        assertThat(response.getPrimaNueva()).isEqualByComparingTo("12600000.00");
        assertThat(response.getIpcAplicado()).isEqualByComparingTo("5.0");
        assertThat(response.getPoliza().getEstado()).isEqualTo(EstadoPoliza.RENOVADA);

        // Debe notificar al CORE el intento de actualizacion.
        verify(registrarEventoCoreService).registrarEvento(any(CoreEventoRequest.class));
    }

    @Test
    @DisplayName("no permite renovar una poliza cancelada")
    void noRenuevaPolizaCancelada() {
        Poliza poliza = polizaActiva();
        poliza.setEstado(EstadoPoliza.CANCELADA);
        when(polizaRepository.findById(1L)).thenReturn(Optional.of(poliza));

        assertThatThrownBy(() -> service.renovar(1L))
                .isInstanceOf(ReglaNegocioException.class)
                .hasMessageContaining("cancelada");

        verify(polizaRepository, never()).save(any());
        verify(registrarEventoCoreService, never()).registrarEvento(any());
    }

    @Test
    @DisplayName("lanza error si la poliza no existe")
    void errorSiNoExiste() {
        when(polizaRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.renovar(99L))
                .isInstanceOf(RecursoNoEncontradoException.class);
    }
}
