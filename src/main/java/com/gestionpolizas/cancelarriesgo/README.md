# cancelarriesgo

Servicio encargado de cancelar un riesgo individual.

> #java #gestion-polizas

---

## Descripción

Cancela un riesgo específico (estado `CANCELADO`) por su identificador. Tras cancelar, notifica
el cambio al CORE (mock).

**Reglas de negocio:**
- Si el riesgo ya está cancelado, se rechaza la operación (responde 409).

---

## Endpoint

#### POST /api/v1/riesgos/{id}/cancelar

Cancela el riesgo indicado por `id`.

**Response OK:**
```json
{
  "dataHeader": { "codRespuesta": 0, "errores": [] },
  "data": {
    "riesgo": {
      "id": 3,
      "polizaId": 2,
      "estado": "CANCELADO",
      "inmuebleDireccion": "Avenida 68 # 22-33 Apto 801, Bogota",
      "canonAsociado": 2200000.00,
      "fechaCancelacion": "2026-10-10"
    }
  }
}
```

**Response error (ya cancelado):**
```json
{
  "dataHeader": {
    "codRespuesta": -1,
    "errores": [{ "codError": "REGLA_NEGOCIO", "descError": "El riesgo ya se encuentra cancelado." }]
  },
  "data": null
}
```

---

## Dependencias

- Base de datos: tabla `riesgo`.
- Servicio `registrareventocore` (notificación al CORE).

---

## Autores

- Olivet Chona - Desarrollador TI
