package com.gestionpolizas.cancelarriesgo.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

import com.gestionpolizas.cancelarriesgo.models.CancelarRiesgoResponse;
import com.gestionpolizas.commons.exception.RecursoNoEncontradoException;
import com.gestionpolizas.commons.exception.ReglaNegocioException;
import com.gestionpolizas.registrareventocore.models.CoreEventoRequest;
import com.gestionpolizas.registrareventocore.service.RegistrarEventoCoreService;
import com.gestionpolizas.commons.entity.EstadoPoliza;
import com.gestionpolizas.commons.entity.EstadoRiesgo;
import com.gestionpolizas.commons.entity.Poliza;
import com.gestionpolizas.commons.entity.Riesgo;
import com.gestionpolizas.commons.entity.TipoPoliza;
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
 * Pruebas unitarias de la logica de cancelacion de un riesgo individual.
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("Cancelar riesgo - reglas de negocio")
class CancelarRiesgoServiceImplTest {

    @Mock
    private RiesgoRepository riesgoRepository;

    @Mock
    private RegistrarEventoCoreService registrarEventoCoreService;

    @InjectMocks
    private CancelarRiesgoServiceImpl service;

    private Riesgo riesgo(EstadoRiesgo estado) {
        Poliza poliza = Poliza.builder()
                .id(1L)
                .tipo(TipoPoliza.COLECTIVA)
                .estado(EstadoPoliza.ACTIVA)
                .build();
        return Riesgo.builder()
                .id(10L)
                .poliza(poliza)
                .estado(estado)
                .inmuebleDireccion("Carrera 7 # 45-10")
                .canonAsociado(new BigDecimal("2000000.00"))
                .fechaCreacion(LocalDateTime.now())
                .build();
    }

    @Test
    @DisplayName("cancela un riesgo activo y notifica al CORE")
    void cancelaRiesgoActivo() {
        when(riesgoRepository.findById(10L)).thenReturn(Optional.of(riesgo(EstadoRiesgo.ACTIVO)));
        when(riesgoRepository.save(any(Riesgo.class))).thenAnswer(inv -> inv.getArgument(0));

        CancelarRiesgoResponse response = service.cancelar(10L);

        assertThat(response.getRiesgo().getEstado()).isEqualTo(EstadoRiesgo.CANCELADO);
        assertThat(response.getRiesgo().getFechaCancelacion()).isNotNull();
        verify(registrarEventoCoreService).registrarEvento(any(CoreEventoRequest.class));
    }

    @Test
    @DisplayName("rechaza cancelar un riesgo ya cancelado")
    void rechazaRiesgoYaCancelado() {
        when(riesgoRepository.findById(10L)).thenReturn(Optional.of(riesgo(EstadoRiesgo.CANCELADO)));

        assertThatThrownBy(() -> service.cancelar(10L))
                .isInstanceOf(ReglaNegocioException.class)
                .hasMessageContaining("ya se encuentra cancelado");

        verify(riesgoRepository, never()).save(any());
    }

    @Test
    @DisplayName("lanza error si el riesgo no existe")
    void errorSiNoExiste() {
        when(riesgoRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.cancelar(99L))
                .isInstanceOf(RecursoNoEncontradoException.class);
    }
}
