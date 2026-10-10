# consultarpolizas

Servicio encargado de listar pólizas filtrando opcionalmente por tipo y estado.

> #java #gestion-polizas

---

## Descripción

Consulta las pólizas registradas. Admite dos filtros opcionales: `tipo` (INDIVIDUAL / COLECTIVA) y
`estado` (ACTIVA / RENOVADA / CANCELADA). Si no se envía ningún filtro, devuelve todas las pólizas.

---

## Endpoint

#### GET /api/v1/polizas

Lista pólizas por tipo y estado.

**Query params (opcionales):**

| Parámetro | Valores | Descripción |
|-----------|---------|-------------|
| tipo | INDIVIDUAL, COLECTIVA | Filtra por tipo de póliza |
| estado | ACTIVA, RENOVADA, CANCELADA | Filtra por estado |

**Ejemplo:** `GET /api/v1/polizas?tipo=COLECTIVA&estado=ACTIVA`

**Response OK:**
```json
{
  "dataHeader": { "codRespuesta": 0, "errores": [] },
  "data": {
    "total": 1,
    "polizas": [
      {
        "id": 2,
        "tipo": "COLECTIVA",
        "estado": "ACTIVA",
        "fechaInicioVigencia": "2025-02-01",
        "fechaFinVigencia": "2026-01-31",
        "canonMensual": 2000000.00,
        "numeroMesesVigencia": 12,
        "prima": 24000000.00,
        "ipcAplicado": null
      }
    ]
  }
}
```

---

## Dependencias

- Base de datos: tabla `poliza`.

---

## Autores

- Olivet Chona - Desarrollador TI
