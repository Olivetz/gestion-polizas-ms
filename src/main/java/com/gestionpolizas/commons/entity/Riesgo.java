package com.gestionpolizas.commons.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.math.BigDecimal;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Representa un riesgo (inmueble asegurado) asociado a una póliza.
 *
 * <p>Una póliza individual admite exactamente 1 riesgo; una colectiva admite 1..N.</p>
 */
@Entity
@Table(name = "riesgo")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Riesgo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Póliza a la que pertenece el riesgo. Se mapea con carga perezosa para evitar
     * traer la póliza completa en cada consulta de riesgo.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "poliza_id", nullable = false)
    private Poliza poliza;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private EstadoRiesgo estado;

    /** Dirección o identificación del inmueble asegurado. */
    @Column(name = "inmueble_direccion", nullable = false, length = 300)
    private String inmuebleDireccion;

    /** Canon mensual asociado al inmueble de este riesgo. */
    @Column(name = "canon_asociado", nullable = false, precision = 15, scale = 2)
    private BigDecimal canonAsociado;

    @Column(name = "fecha_creacion", nullable = false)
    private LocalDateTime fechaCreacion;

    @Column(name = "fecha_cancelacion")
    private LocalDate fechaCancelacion;
}
