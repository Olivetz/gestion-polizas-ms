# consultarriesgos

Servicio encargado de listar los riesgos asociados a una póliza.

> #java #gestion-polizas

---

## Descripción

Devuelve los riesgos (inmuebles asegurados) de una póliza. Verifica primero que la póliza exista;
si no existe, responde 404.

---

## Endpoint

#### GET /api/v1/polizas/{id}/riesgos

Lista los riesgos de la póliza indicada por `id`.

**Response OK:**
```json
{
  "dataHeader": { "codRespuesta": 0, "errores": [] },
  "data": {
    "polizaId": 2,
    "total": 2,
    "riesgos": [
      {
        "id": 2,
        "polizaId": 2,
        "estado": "ACTIVO",
        "inmuebleDireccion": "Carrera 7 # 45-10 Local 2, Bogota",
        "canonAsociado": 2000000.00,
        "fechaCancelacion": null
      }
    ]
  }
}
```

**Response error (póliza inexistente):**
```json
{
  "dataHeader": {
    "codRespuesta": -1,
    "errores": [{ "codError": "RECURSO_NO_ENCONTRADO", "descError": "No existe la póliza con id 9999" }]
  },
  "data": null
}
```

---

## Dependencias

- Base de datos: tablas `poliza` y `riesgo`.

---

## Autores

- Olivet Chona - Desarrollador TI
