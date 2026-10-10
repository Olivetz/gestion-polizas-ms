# gestion-polizas-ms

Microservicio REST para la gestión de pólizas de arrendamiento de inmuebles (individuales y
colectivas) y sus riesgos asociados.

> Stack: **Java 21 · Spring Boot 3.4 · Spring Data JPA · H2 / PostgreSQL · Gradle**

---

## Tabla de contenido

- [Descripción](#descripción)
- [Arquitectura y organización del código](#arquitectura-y-organización-del-código)
- [Requisitos previos](#requisitos-previos)
- [Cómo ejecutar](#cómo-ejecutar)
- [Seguridad (API Key)](#seguridad-api-key)
- [Endpoints](#endpoints)
- [Colección de Postman](#colección-de-postman)
- [Ejemplos de uso (curl)](#ejemplos-de-uso-curl)
- [Reglas de negocio implementadas](#reglas-de-negocio-implementadas)
- [Pruebas](#pruebas)
- [Decisiones técnicas](#decisiones-técnicas)

---

## Descripción

El negocio maneja dos tipos de pólizas de arrendamiento:

- **Individual:** el tomador y asegurado es el arrendatario; el beneficiario es el arrendador.
  Admite **un solo riesgo**.
- **Colectiva:** orientada a inmobiliarias y administraciones; asegura a los arrendatarios y los
  beneficiarios son los arrendadores. Admite **uno o varios riesgos**.

Toda póliza tiene un periodo de vigencia, un canon mensual y una prima
(`prima = canon mensual × número de meses de vigencia`). Las pólizas pueden renovarse ajustando el
canon según el **IPC**. Las operaciones que cambian el estado de una póliza o riesgo notifican al
**CORE** de seguros a través de un mock (ver `registrareventocore`).

---

## Arquitectura y organización del código

El código se organiza **por caso de uso** (vertical slicing): cada operación tiene su propio paquete
con sus capas, y cada uno incluye su propio `README.md`. El código compartido entre casos de uso
(entidades, repositorios, DTOs comunes y excepciones) vive en `commons`.

```
com.gestionpolizas
├── GestionPolizasApplication.java      # Clase principal
├── commons/
│   ├── dto/            # ApiResponse<T>, DataHeader, BusinessError (respuesta uniforme)
│   ├── entity/         # Poliza, Riesgo, enums (modelo de dominio compartido)
│   ├── repository/     # PolizaRepository, RiesgoRepository (compartidos entre casos de uso)
│   └── exception/      # Excepciones de negocio + GlobalExceptionHandler
├── config/
│   ├── security/       # ApiKeyFilter, SecurityHeadersFilter
│   └── OpenApiConfig
├── consultarpolizas/     # GET  /polizas
├── consultarriesgos/     # GET  /polizas/{id}/riesgos
├── renovarpoliza/        # POST /polizas/{id}/renovar
├── cancelarpoliza/       # POST /polizas/{id}/cancelar
├── agregarriesgo/        # POST /polizas/{id}/riesgos
├── cancelarriesgo/       # POST /riesgos/{id}/cancelar
└── registrareventocore/  # POST /core-mock/evento (mock del CORE)
```

Cada caso de uso contiene: `controller/`, `service/` (interfaz + `Impl`), `models/` (request y
response del endpoint) y `dto/` (clases complementarias como `PolizaDto`, `RiesgoDto`).

> **Nota sobre `commons/repository` y `commons/entity`:** las entidades y repositorios se ubican en
> `commons` porque son transversales y se usan desde la mayoría de los casos de uso. Centralizarlos
> ahí evita duplicarlos en cada feature y mantiene una única definición del modelo de dominio.

---

## Requisitos previos

- **JDK 21** (configurado en el IDE).
- No requiere instalar base de datos: por defecto usa **H2 en memoria**.
- (Opcional) PostgreSQL si se desea ejecutar con el perfil `postgres`.

> El proyecto incluye el **Gradle Wrapper** (`gradlew`), por lo que no es necesario instalar Gradle.

---

## Cómo ejecutar

### Opción A — H2 en memoria (por defecto, recomendada)

No requiere instalar ni configurar nada. La base se crea en memoria al arrancar y se carga con datos
de ejemplo automáticamente.

**Desde IntelliJ:** abrir el proyecto, seleccionar JDK 21 y ejecutar `GestionPolizasApplication`.

**Desde terminal:**
```bash
./gradlew bootRun
```

La aplicación queda disponible en `http://localhost:8080`.

- **Swagger UI:** http://localhost:8080/swagger-ui.html
- **Consola H2:** http://localhost:8080/h2-console
  (JDBC URL: `jdbc:h2:mem:gestion_polizas`, usuario `sa`, sin contraseña)

### Opción B — PostgreSQL (perfil `postgres`)

1. Crear la base y las tablas ejecutando los scripts de la carpeta `db/`:
   - `db/schema.sql` (estructura)
   - `db/data.sql` (datos de ejemplo)
2. Definir las variables de entorno de conexión (ejemplo):
   ```bash
   set DB_URL=jdbc:postgresql://localhost:5432/gestion_polizas
   set DB_USERNAME=mi_usuario
   set DB_PASSWORD=mi_clave
   ```
3. Arrancar con el perfil `postgres`:
   ```bash
   ./gradlew bootRun --args="--spring.profiles.active=postgres"
   ```

---

## Seguridad (API Key)

Todas las rutas de negocio exigen el header:

```
api-key: 123456
```

El valor se resuelve desde configuración (`security.api-key`), nunca está hardcodeado en el código;
el default `123456` es solo para el ejercicio. Las rutas de documentación (Swagger), health y consola
H2 están exentas. Las peticiones sin api-key válida reciben **401 Unauthorized**.

---

## Endpoints

Base URL: `http://localhost:8080/api/v1`

| Método | Ruta | Descripción |
|--------|------|-------------|
| GET  | `/polizas?tipo={tipo}&estado={estado}` | Lista pólizas (filtros opcionales) |
| GET  | `/polizas/{id}/riesgos`                | Lista los riesgos de una póliza |
| POST | `/polizas/{id}/renovar`                | Renueva la póliza (ajuste por IPC) |
| POST | `/polizas/{id}/cancelar`               | Cancela la póliza y sus riesgos |
| POST | `/polizas/{id}/riesgos`                | Agrega un riesgo (solo pólizas colectivas) |
| POST | `/riesgos/{id}/cancelar`               | Cancela un riesgo |
| POST | `/core-mock/evento`                    | Mock del CORE (registra el evento en logs) |

Valores de filtro: `tipo` = `INDIVIDUAL` \| `COLECTIVA`; `estado` = `ACTIVA` \| `RENOVADA` \| `CANCELADA`.

> Cada funcionalidad tiene su propio `README.md` dentro de su paquete, con el detalle del endpoint,
> request/response y reglas de negocio.

---

## Colección de Postman

En la carpeta **`postmanCollections/`** del repositorio se incluye una colección lista para importar:

```
postmanCollections/gestion-polizas-ms.postman_collection.json
```

Para usarla: abrir Postman → **Import** → seleccionar ese archivo. La colección trae todos los
endpoints organizados por carpetas (Pólizas, Riesgos, Core Mock y Casos de error), con el header
`api-key` ya configurado y ejemplos de cuerpo listos para probar los casos de éxito y de error.

---

## Ejemplos de uso (curl)

> **Datos de ejemplo precargados** (cada póliza está pensada para probar un endpoint sin interferir
> con los demás):
> - Póliza 1 — INDIVIDUAL, ACTIVA (consultas)
> - Póliza 2 — COLECTIVA, ACTIVA (consultar riesgos / agregar riesgo)
> - Póliza 3 — COLECTIVA, CANCELADA (probar error al renovar)
> - Póliza 4 — INDIVIDUAL, ACTIVA (probar renovar)
> - Póliza 5 — COLECTIVA, ACTIVA (probar cancelar, con riesgos en cascada)
> - Póliza 6 — INDIVIDUAL, ACTIVA (probar error al agregar riesgo a individual)

**Listar pólizas colectivas activas:**
```bash
curl -H "api-key: 123456" \
  "http://localhost:8080/api/v1/polizas?tipo=COLECTIVA&estado=ACTIVA"
```

**Consultar riesgos de la póliza 2:**
```bash
curl -H "api-key: 123456" \
  "http://localhost:8080/api/v1/polizas/2/riesgos"
```

**Renovar la póliza 4:**
```bash
curl -X POST -H "api-key: 123456" \
  "http://localhost:8080/api/v1/polizas/4/renovar"
```

**Agregar un riesgo a la póliza colectiva 2:**
```bash
curl -X POST -H "api-key: 123456" -H "Content-Type: application/json" \
  -d "{\"inmuebleDireccion\":\"Calle 50 # 10-20\",\"canonAsociado\":1900000}" \
  "http://localhost:8080/api/v1/polizas/2/riesgos"
```

**Cancelar la póliza 5 (cancela sus riesgos en cascada):**
```bash
curl -X POST -H "api-key: 123456" \
  "http://localhost:8080/api/v1/polizas/5/cancelar"
```

**Enviar un evento al CORE mock:**
```bash
curl -X POST -H "api-key: 123456" -H "Content-Type: application/json" \
  -d "{\"evento\":\"ACTUALIZACION\",\"polizaId\":555}" \
  "http://localhost:8080/api/v1/core-mock/evento"
```

---

## Reglas de negocio implementadas

1. **Una póliza individual solo puede tener 1 riesgo.** El endpoint de agregar riesgo solo acepta
   pólizas colectivas (ver nota en Decisiones técnicas).
2. **No se puede renovar una póliza cancelada.** Se rechaza con error de negocio.
3. **La cancelación de una póliza cancela todos sus riesgos** (en cascada).
4. **Agregar riesgo exige validación del tipo de póliza.**

Todas las operaciones que modifican estado notifican al CORE (mock) el intento de sincronización.

---

## Pruebas

Pruebas unitarias de las reglas de negocio (JUnit 5 + Mockito):

```bash
./gradlew test
```

Cubren: renovación con ajuste por IPC y bloqueo de canceladas, validación de tipo al agregar riesgo,
y cancelación en cascada de riesgos.

---

## Decisiones técnicas

- **H2 por defecto, PostgreSQL opcional.** Dejé H2 en memoria como perfil por defecto para que el
  proyecto arranque sin tener que instalar ni configurar una base de datos. El perfil `postgres`
  queda listo para usar una base real, como sería en producción.
- **Agregar riesgo solo en pólizas colectivas.** Seguí la indicación del enunciado ("Solo si tipo =
  Colectiva") y dejé que este endpoint rechace las pólizas individuales. El riesgo de una póliza
  individual se crea junto con la póliza, por lo que no entra por aquí.
- **IPC configurable.** El porcentaje de IPC de la renovación lo manejo como parámetro
  (`negocio.ipc-porcentaje`) en lugar de dejarlo quemado en el código.
- **Respuesta uniforme.** Todas las respuestas usan `ApiResponse<T>` con la estructura
  `{ dataHeader: { codRespuesta, errores }, data }` (`codRespuesta = 0` éxito, `-1` error). Los
  errores los centralicé en un `@RestControllerAdvice` para no exponer stack traces, y cada error de
  validación va como un elemento aparte en la lista `errores`.
- **Seguridad.** Filtro de API Key (el valor viene de configuración, no se escribe en logs) y headers
  de seguridad (HSTS, CSP, X-Frame-Options, etc.).
- **Librerías estándar.** Usé librerías open source estándar en lugar de las internas de la compañía,
  para que el proyecto se pueda ejecutar fuera del entorno corporativo. En un entorno real estas se
  reemplazarían por las librerías institucionales (manejo de errores, logs centralizados, parameter
  store, documentación Swagger).
```
