# cancelarpoliza

Servicio encargado de cancelar una póliza y, en cascada, todos sus riesgos activos.

> #java #gestion-polizas

---

## Descripción

Cancela una póliza (estado `CANCELADA`) y marca como `CANCELADO` todos sus riesgos activos.
Tras cancelar, notifica el cambio al CORE (mock).

**Reglas de negocio:**
- Si la póliza ya está cancelada, se rechaza la operación (responde 409).
- La cancelación de la póliza cancela en cascada todos sus riesgos.

---

## Endpoint

#### POST /api/v1/polizas/{id}/cancelar

Cancela la póliza indicada por `id`.

**Response OK:**
```json
{
  "dataHeader": { "codRespuesta": 0, "errores": [] },
  "data": {
    "poliza": {
      "id": 5,
      "tipo": "COLECTIVA",
      "estado": "CANCELADA"
    },
    "riesgosCancelados": 2
  }
}
```

**Response error (ya cancelada):**
```json
{
  "dataHeader": {
    "codRespuesta": -1,
    "errores": [{ "codError": "REGLA_NEGOCIO", "descError": "La póliza ya se encuentra cancelada." }]
  },
  "data": null
}
```

---

## Dependencias

- Base de datos: tablas `poliza` y `riesgo`.
- Servicio `registrareventocore` (notificación al CORE).

---

## Autores

- Olivet Chona - Desarrollador TI
