package com.gestionpolizas.registrareventocore.service;

import com.gestionpolizas.registrareventocore.models.CoreEventoRequest;
import com.gestionpolizas.registrareventocore.models.CoreEventoResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * Implementacion del registro de eventos en el CORE (mock). Simula el envio de un evento al
 * sistema legado registrando la operacion en los logs. No persiste nada: representa el borde de
 * integracion (Adapter al CORE) descrito en el diseno de arquitectura.
 */
@Service
public class RegistrarEventoCoreServiceImpl implements RegistrarEventoCoreService {

    private static final Logger log = LoggerFactory.getLogger(RegistrarEventoCoreServiceImpl.class);

    @Override
    public CoreEventoResponse registrarEvento(CoreEventoRequest request) {
        log.info("[CORE-MOCK] Intento de envio al CORE legado - evento: '{}', polizaId: {}",
                request.getEvento(), request.getPolizaId());

        return CoreEventoResponse.builder()
                .recibido(true)
                .evento(request.getEvento())
                .polizaId(request.getPolizaId())
                .mensaje("Evento recibido y registrado por el CORE mock.")
                .build();
    }
}
