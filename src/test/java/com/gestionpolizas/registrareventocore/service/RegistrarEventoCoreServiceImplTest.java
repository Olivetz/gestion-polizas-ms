package com.gestionpolizas.registrareventocore.service;

import com.gestionpolizas.registrareventocore.models.CoreEventoRequest;
import com.gestionpolizas.registrareventocore.models.CoreEventoResponse;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Pruebas unitarias del mock del CORE.
 */
@DisplayName("Registrar evento CORE (mock)")
class RegistrarEventoCoreServiceImplTest {

    private RegistrarEventoCoreService service;

    @BeforeEach
    void setUp() {
        service = new RegistrarEventoCoreServiceImpl();
    }

    @Test
    @DisplayName("confirma la recepcion del evento con los mismos datos enviados")
    void registraEvento() {
        CoreEventoRequest request = CoreEventoRequest.builder()
                .evento("ACTUALIZACION")
                .polizaId(555L)
                .build();

        CoreEventoResponse response = service.registrarEvento(request);

        assertThat(response.isRecibido()).isTrue();
        assertThat(response.getEvento()).isEqualTo("ACTUALIZACION");
        assertThat(response.getPolizaId()).isEqualTo(555L);
        assertThat(response.getMensaje()).isNotBlank();
    }
}
