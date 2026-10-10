package com.gestionpolizas.consultarriesgos.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import com.gestionpolizas.commons.exception.RecursoNoEncontradoException;
import com.gestionpolizas.consultarriesgos.models.ConsultarRiesgosResponse;
import com.gestionpolizas.commons.entity.EstadoRiesgo;
import com.gestionpolizas.commons.entity.Riesgo;
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
import static org.mockito.Mockito.when;

/**
 * Pruebas unitarias de la logica de consulta de riesgos por poliza.
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("Consultar riesgos - por poliza")
class ConsultarRiesgosServiceImplTest {

    @Mock
    private RiesgoRepository riesgoRepository;

    @Mock
    private PolizaRepository polizaRepository;

    @InjectMocks
    private ConsultarRiesgosServiceImpl service;

    private Riesgo riesgo(Long id) {
        return Riesgo.builder()
                .id(id)
                .estado(EstadoRiesgo.ACTIVO)
                .inmuebleDireccion("Direccion " + id)
                .canonAsociado(new BigDecimal("1000000.00"))
                .fechaCreacion(LocalDateTime.now())
                .build();
    }

    @Test
    @DisplayName("devuelve los riesgos de una poliza existente")
    void devuelveRiesgos() {
        when(polizaRepository.existsById(2L)).thenReturn(true);
        when(riesgoRepository.findByPolizaId(2L))
                .thenReturn(List.of(riesgo(1L), riesgo(2L)));

        ConsultarRiesgosResponse response = service.consultarPorPoliza(2L);

        assertThat(response.getPolizaId()).isEqualTo(2L);
        assertThat(response.getTotal()).isEqualTo(2);
        assertThat(response.getRiesgos()).hasSize(2);
    }

    @Test
    @DisplayName("lanza error si la poliza no existe")
    void errorSiPolizaNoExiste() {
        when(polizaRepository.existsById(9999L)).thenReturn(false);

        assertThatThrownBy(() -> service.consultarPorPoliza(9999L))
                .isInstanceOf(RecursoNoEncontradoException.class)
                .hasMessageContaining("9999");
    }
}
