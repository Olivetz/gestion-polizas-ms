# renovarpoliza

Servicio encargado de renovar una póliza ajustando el canon y la prima según el IPC.

> #java #gestion-polizas

---

## Descripción

Renueva una póliza: incrementa el canon mensual aplicando el IPC configurado
(`canonNuevo = canon * (1 + IPC/100)`), recalcula la prima (`canonNuevo * numeroMesesVigencia`),
extiende la vigencia por el mismo periodo y deja la póliza en estado `RENOVADA`. Tras renovar,
notifica el cambio al CORE (mock).

**Reglas de negocio:**
- No se puede renovar una póliza en estado `CANCELADA` (responde 409).

El porcentaje de IPC es configurable mediante la propiedad `negocio.ipc-porcentaje` (por defecto 5.0%).

---

## Endpoint

#### POST /api/v1/polizas/{id}/renovar

Renueva la póliza indicada por `id`.

**Response OK:**
```json
{
  "dataHeader": { "codRespuesta": 0, "errores": [] },
  "data": {
    "poliza": {
      "id": 4,
      "tipo": "INDIVIDUAL",
      "estado": "RENOVADA",
      "canonMensual": 1050000.00,
      "prima": 12600000.00,
      "ipcAplicado": 5.0
    },
    "ipcAplicado": 5.0,
    "canonAnterior": 1000000.00,
    "canonNuevo": 1050000.00,
    "primaAnterior": 12000000.00,
    "primaNueva": 12600000.00
  }
}
```

**Response error (póliza cancelada):**
```json
{
  "dataHeader": {
    "codRespuesta": -1,
    "errores": [{ "codError": "REGLA_NEGOCIO", "descError": "No se puede renovar una póliza cancelada." }]
  },
  "data": null
}
```

---

## Dependencias

- Base de datos: tabla `poliza`.
- Servicio `registrareventocore` (notificación al CORE).

---

## Autores

- Olivet Chona - Desarrollador TI
