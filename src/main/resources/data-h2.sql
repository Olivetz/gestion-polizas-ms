-- =====================================================================================
-- Datos de ejemplo para el perfil H2 (en memoria).
-- Se ejecuta automáticamente al arrancar la aplicación, después de schema-h2.sql.
--
-- Cada póliza está pensada para probar un endpoint específico sin que las pruebas
-- interfieran entre sí (ej. una póliza dedicada a renovar y otra a cancelar):
--   Póliza 1  INDIVIDUAL ACTIVA     -> consultas generales
--   Póliza 2  COLECTIVA  ACTIVA     -> consultar riesgos / agregar riesgo
--   Póliza 3  COLECTIVA  CANCELADA  -> probar error "renovar póliza cancelada"
--   Póliza 4  INDIVIDUAL ACTIVA     -> probar renovar
--   Póliza 5  COLECTIVA  ACTIVA     -> probar cancelar (con riesgos en cascada)
--   Póliza 6  INDIVIDUAL ACTIVA     -> probar error "agregar riesgo a individual"
-- =====================================================================================

-- Póliza 1: INDIVIDUAL, ACTIVA. Canon 1.500.000 x 12 meses => prima 18.000.000.
INSERT INTO poliza (tipo, estado, tomador_id, asegurado_id, beneficiario_id,
                    fecha_inicio_vigencia, fecha_fin_vigencia, canon_mensual,
                    numero_meses_vigencia, prima, ipc_aplicado, fecha_creacion)
VALUES ('INDIVIDUAL', 'ACTIVA', 'CC-1001', 'CC-1001', 'CC-2001',
        DATE '2025-01-01', DATE '2025-12-31', 1500000.00, 12, 18000000.00, NULL, CURRENT_TIMESTAMP);

-- Póliza 2: COLECTIVA, ACTIVA. Canon de referencia 2.000.000 x 12 => prima 24.000.000.
INSERT INTO poliza (tipo, estado, tomador_id, asegurado_id, beneficiario_id,
                    fecha_inicio_vigencia, fecha_fin_vigencia, canon_mensual,
                    numero_meses_vigencia, prima, ipc_aplicado, fecha_creacion)
VALUES ('COLECTIVA', 'ACTIVA', 'NIT-900100200', NULL, NULL,
        DATE '2025-02-01', DATE '2026-01-31', 2000000.00, 12, 24000000.00, NULL, CURRENT_TIMESTAMP);

-- Póliza 3: COLECTIVA, CANCELADA. Para probar que no se puede renovar.
INSERT INTO poliza (tipo, estado, tomador_id, asegurado_id, beneficiario_id,
                    fecha_inicio_vigencia, fecha_fin_vigencia, canon_mensual,
                    numero_meses_vigencia, prima, ipc_aplicado, fecha_creacion)
VALUES ('COLECTIVA', 'CANCELADA', 'NIT-900300400', NULL, NULL,
        DATE '2024-06-01', DATE '2025-05-31', 1800000.00, 12, 21600000.00, NULL, CURRENT_TIMESTAMP);

-- Póliza 4: INDIVIDUAL, ACTIVA. Dedicada a probar la renovación.
INSERT INTO poliza (tipo, estado, tomador_id, asegurado_id, beneficiario_id,
                    fecha_inicio_vigencia, fecha_fin_vigencia, canon_mensual,
                    numero_meses_vigencia, prima, ipc_aplicado, fecha_creacion)
VALUES ('INDIVIDUAL', 'ACTIVA', 'CC-1004', 'CC-1004', 'CC-2004',
        DATE '2025-03-01', DATE '2026-02-28', 1000000.00, 12, 12000000.00, NULL, CURRENT_TIMESTAMP);

-- Póliza 5: COLECTIVA, ACTIVA. Dedicada a probar la cancelación (con riesgos en cascada).
INSERT INTO poliza (tipo, estado, tomador_id, asegurado_id, beneficiario_id,
                    fecha_inicio_vigencia, fecha_fin_vigencia, canon_mensual,
                    numero_meses_vigencia, prima, ipc_aplicado, fecha_creacion)
VALUES ('COLECTIVA', 'ACTIVA', 'NIT-900500600', NULL, NULL,
        DATE '2025-04-01', DATE '2026-03-31', 2500000.00, 12, 30000000.00, NULL, CURRENT_TIMESTAMP);

-- Póliza 6: INDIVIDUAL, ACTIVA. Para probar el error al agregar riesgo a una individual.
INSERT INTO poliza (tipo, estado, tomador_id, asegurado_id, beneficiario_id,
                    fecha_inicio_vigencia, fecha_fin_vigencia, canon_mensual,
                    numero_meses_vigencia, prima, ipc_aplicado, fecha_creacion)
VALUES ('INDIVIDUAL', 'ACTIVA', 'CC-1006', 'CC-1006', 'CC-2006',
        DATE '2025-05-01', DATE '2026-04-30', 1700000.00, 12, 20400000.00, NULL, CURRENT_TIMESTAMP);

-- -------------------------------------------------------------------------------------
-- Riesgos
-- -------------------------------------------------------------------------------------
-- Riesgo de la póliza INDIVIDUAL (1) — único riesgo permitido.
INSERT INTO riesgo (poliza_id, estado, inmueble_direccion, canon_asociado, fecha_creacion)
VALUES (1, 'ACTIVO', 'Calle 100 # 15-20 Apto 301, Bogota', 1500000.00, CURRENT_TIMESTAMP);

-- Riesgos de la póliza COLECTIVA (2) — admite varios. (ids 2 y 3)
INSERT INTO riesgo (poliza_id, estado, inmueble_direccion, canon_asociado, fecha_creacion)
VALUES (2, 'ACTIVO', 'Carrera 7 # 45-10 Local 2, Bogota', 2000000.00, CURRENT_TIMESTAMP);

INSERT INTO riesgo (poliza_id, estado, inmueble_direccion, canon_asociado, fecha_creacion)
VALUES (2, 'ACTIVO', 'Avenida 68 # 22-33 Apto 801, Bogota', 2200000.00, CURRENT_TIMESTAMP);

-- Riesgos de la póliza COLECTIVA (5) — para ver la cancelación en cascada. (ids 4 y 5)
INSERT INTO riesgo (poliza_id, estado, inmueble_direccion, canon_asociado, fecha_creacion)
VALUES (5, 'ACTIVO', 'Calle 80 # 12-34 Apto 1002, Bogota', 2500000.00, CURRENT_TIMESTAMP);

INSERT INTO riesgo (poliza_id, estado, inmueble_direccion, canon_asociado, fecha_creacion)
VALUES (5, 'ACTIVO', 'Carrera 50 # 100-25 Local 5, Bogota', 2600000.00, CURRENT_TIMESTAMP);
