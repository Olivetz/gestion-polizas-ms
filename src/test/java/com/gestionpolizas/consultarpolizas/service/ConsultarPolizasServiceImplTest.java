package com.gestionpolizas.consultarpolizas.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import com.gestionpolizas.consultarpolizas.models.ConsultarPolizasResponse;
import com.gestionpolizas.commons.entity.EstadoPoliza;
import com.gestionpolizas.commons.entity.Poliza;
import com.gestionpolizas.commons.entity.TipoPoliza;
import com.gestionpolizas.commons.repository.PolizaRepository;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Pruebas unitarias de la logica de consulta de polizas (seleccion de filtros).
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("Consultar polizas - filtros")
class ConsultarPolizasServiceImplTest {

    @Mock
    private PolizaRepository polizaRepository;

    @InjectMocks
    private ConsultarPolizasServiceImpl service;

    private Poliza poliza(Long id, TipoPoliza tipo, EstadoPoliza estado) {
        return Poliza.builder()
                .id(id)
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
    @DisplayName("sin filtros devuelve todas las polizas")
    void sinFiltros() {
        when(polizaRepository.findAll())
                .thenReturn(List.of(poliza(1L, TipoPoliza.INDIVIDUAL, EstadoPoliza.ACTIVA),
                        poliza(2L, TipoPoliza.COLECTIVA, EstadoPoliza.ACTIVA)));

        ConsultarPolizasResponse response = service.consultar(null, null);

        assertThat(response.getTotal()).isEqualTo(2);
        assertThat(response.getPolizas()).hasSize(2);
        verify(polizaRepository).findAll();
    }

    @Test
    @DisplayName("con tipo y estado usa el filtro combinado")
    void conTipoYEstado() {
        when(polizaRepository.findByTipoAndEstado(TipoPoliza.COLECTIVA, EstadoPoliza.ACTIVA))
                .thenReturn(List.of(poliza(2L, TipoPoliza.COLECTIVA, EstadoPoliza.ACTIVA)));

        ConsultarPolizasResponse response = service.consultar(TipoPoliza.COLECTIVA, EstadoPoliza.ACTIVA);

        assertThat(response.getTotal()).isEqualTo(1);
        assertThat(response.getPolizas().get(0).getTipo()).isEqualTo(TipoPoliza.COLECTIVA);
        verify(polizaRepository).findByTipoAndEstado(TipoPoliza.COLECTIVA, EstadoPoliza.ACTIVA);
    }

    @Test
    @DisplayName("con solo tipo usa el filtro por tipo")
    void soloTipo() {
        when(polizaRepository.findByTipo(TipoPoliza.INDIVIDUAL))
                .thenReturn(List.of(poliza(1L, TipoPoliza.INDIVIDUAL, EstadoPoliza.ACTIVA)));

        ConsultarPolizasResponse response = service.consultar(TipoPoliza.INDIVIDUAL, null);

        assertThat(response.getTotal()).isEqualTo(1);
        verify(polizaRepository).findByTipo(TipoPoliza.INDIVIDUAL);
    }

    @Test
    @DisplayName("con solo estado usa el filtro por estado")
    void soloEstado() {
        when(polizaRepository.findByEstado(EstadoPoliza.CANCELADA))
                .thenReturn(List.of(poliza(3L, TipoPoliza.COLECTIVA, EstadoPoliza.CANCELADA)));

        ConsultarPolizasResponse response = service.consultar(null, EstadoPoliza.CANCELADA);

        assertThat(response.getTotal()).isEqualTo(1);
        verify(polizaRepository).findByEstado(EstadoPoliza.CANCELADA);
    }
}
