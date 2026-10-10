# registrareventocore

Servicio que simula la integración con el CORE de seguros legado (mock).

> #java #gestion-polizas

---

## Descripción

Simula la conexión con el CORE legado. En este ejercicio solo registra en logs que la operación
se intentó enviar al CORE; no guarda datos.

Lo usan internamente las operaciones que cambian el estado de pólizas o riesgos (renovar, cancelar,
agregar y cancelar riesgo), y también se expone como endpoint propio porque el enunciado lo pide.

---

## Endpoint

#### POST /api/v1/core-mock/evento

Registra en logs el intento de envío de un evento al CORE.

**Request:**
```json
{
  "evento": "ACTUALIZACION",
  "polizaId": 555
}
```

**Response OK:**
```json
{
  "dataHeader": { "codRespuesta": 0, "errores": [] },
  "data": {
    "recibido": true,
    "evento": "ACTUALIZACION",
    "polizaId": 555,
    "mensaje": "Evento recibido y registrado por el CORE mock."
  }
}
```

---

## Dependencias

- Ninguna (servicio simulado, solo registra en logs).

---

## Autores

- Olivet Chona - Desarrollador TI
