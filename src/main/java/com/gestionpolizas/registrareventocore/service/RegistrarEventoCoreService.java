package com.gestionpolizas.registrareventocore.service;

import com.gestionpolizas.registrareventocore.models.CoreEventoRequest;
import com.gestionpolizas.registrareventocore.models.CoreEventoResponse;

/**
 * Servicio que simula la integracion con el CORE de seguros legado (a traves del servicio
 * agnostico de edicion en la capa media). En este ejercicio su unico proposito es registrar en
 * logs que la operacion se intento enviar al CORE.
 */
public interface RegistrarEventoCoreService {

    /**
     * Registra el intento de envio de un evento al CORE.
     *
     * @param request datos del evento (tipo y poliza afectada)
     * @return confirmacion de recepcion del evento
     */
    CoreEventoResponse registrarEvento(CoreEventoRequest request);
}
