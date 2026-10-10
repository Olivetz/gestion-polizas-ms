package com.gestionpolizas.agregarriesgo.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

import com.gestionpolizas.agregarriesgo.models.AgregarRiesgoRequest;
import com.gestionpolizas.agregarriesgo.models.AgregarRiesgoResponse;
import com.gestionpolizas.commons.exception.ReglaNegocioException;
import com.gestionpolizas.registrareventocore.models.CoreEventoRequest;
import com.gestionpolizas.registrareventocore.service.RegistrarEventoCoreService;
import com.gestionpolizas.commons.entity.EstadoPoliza;
import com.gestionpolizas.commons.entity.Poliza;
import com.gestionpolizas.commons.entity.Riesgo;
import com.gestionpolizas.commons.entity.TipoPoliza;
import com.gestionpolizas.commons.repository.PolizaRepository;
import com.gestionpolizas.commons.repository.RiesgoRepository;

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
 * Pruebas unitarias de la logica de adicion de riesgos.
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("Agregar riesgo - reglas de negocio")
class AgregarRiesgoServiceImplTest {

    @Mock
    private PolizaRepository polizaRepository;

    @Mock
    private RiesgoRepository riesgoRepository;

    @Mock
    private RegistrarEventoCoreService registrarEventoCoreService;

    @InjectMocks
    private AgregarRiesgoServiceImpl service;

    private AgregarRiesgoRequest request() {
        return AgregarRiesgoRequest.builder()
                .inmuebleDireccion("Calle 123 # 45-67")
                .canonAsociado(new BigDecimal("1800000.00"))
                .build();
    }

    private Poliza poliza(TipoPoliza tipo, EstadoPoliza estado) {
        return Poliza.builder()
                .id(1L)
                .tipo(tipo)
                .estado(estado)
                .fechaInicioVigencia(LocalDate.of(2025, 1, 1))
                .fechaFinVigencia(LocalDate.of(2025, 12, 31))
                .canonMensual(new BigDecimal("1000000.00"))
                .numeroMesesVigencia(12)
                .prima(new BigDecimal("12000000.00"))
                .fechaCreacion(LocalDateTime.now())
                .build();
    }

    @Test
    @DisplayName("agrega un riesgo a una poliza colectiva")
    void agregaRiesgoAColectiva() {
        when(polizaRepository.findById(1L))
                .thenReturn(Optional.of(poliza(TipoPoliza.COLECTIVA, EstadoPoliza.ACTIVA)));
        when(riesgoRepository.save(any(Riesgo.class))).thenAnswer(inv -> {
            Riesgo r = inv.getArgument(0);
            r.setId(10L);
            return r;
        });

        AgregarRiesgoResponse response = service.agregar(1L, request());

        assertThat(response.getRiesgo()).isNotNull();
        assertThat(response.getRiesgo().getId()).isEqualTo(10L);
        verify(registrarEventoCoreService).registrarEvento(any(CoreEventoRequest.class));
    }

    @Test
    @DisplayName("rechaza agregar riesgo a una poliza individual (solo colectivas)")
    void rechazaRiesgoEnIndividual() {
        when(polizaRepository.findById(1L))
                .thenReturn(Optional.of(poliza(TipoPoliza.INDIVIDUAL, EstadoPoliza.ACTIVA)));

        assertThatThrownBy(() -> service.agregar(1L, request()))
                .isInstanceOf(ReglaNegocioException.class)
                .hasMessageContaining("colectivas");

        verify(riesgoRepository, never()).save(any());
    }

    @Test
    @DisplayName("rechaza agregar riesgo a una poliza cancelada")
    void rechazaRiesgoEnCancelada() {
        when(polizaRepository.findById(1L))
                .thenReturn(Optional.of(poliza(TipoPoliza.COLECTIVA, EstadoPoliza.CANCELADA)));

        assertThatThrownBy(() -> service.agregar(1L, request()))
                .isInstanceOf(ReglaNegocioException.class)
                .hasMessageContaining("cancelada");

        verify(riesgoRepository, never()).save(any());
    }
}
