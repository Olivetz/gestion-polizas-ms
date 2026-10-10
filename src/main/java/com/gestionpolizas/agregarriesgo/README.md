# agregarriesgo

Servicio encargado de agregar un riesgo (inmueble asegurado) a una póliza colectiva.

> #java #gestion-polizas

---

## Descripción

Agrega un riesgo a una póliza. Tras agregarlo, notifica el cambio al CORE (mock).

**Reglas de negocio:**
- Solo las pólizas de tipo `COLECTIVA` admiten agregar riesgos por este endpoint
  (según el requerimiento "Solo si tipo = Colectiva"). Las pólizas individuales se rechazan (409).
- No se pueden agregar riesgos a una póliza cancelada (409).
- Los datos del riesgo se validan en servidor (dirección obligatoria, canon mayor a 0).

---

## Endpoint

#### POST /api/v1/polizas/{id}/riesgos

Agrega un riesgo a la póliza colectiva indicada por `id`.

**Request:**
```json
{
  "inmuebleDireccion": "Calle 50 # 10-20 Apto 405, Bogota",
  "canonAsociado": 1900000
}
```

**Response OK (201):**
```json
{
  "dataHeader": { "codRespuesta": 0, "errores": [] },
  "data": {
    "riesgo": {
      "id": 6,
      "polizaId": 2,
      "estado": "ACTIVO",
      "inmuebleDireccion": "Calle 50 # 10-20 Apto 405, Bogota",
      "canonAsociado": 1900000.00,
      "fechaCancelacion": null
    }
  }
}
```

**Response error (póliza individual):**
```json
{
  "dataHeader": {
    "codRespuesta": -1,
    "errores": [{ "codError": "REGLA_NEGOCIO", "descError": "Solo las pólizas colectivas admiten agregar riesgos." }]
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
