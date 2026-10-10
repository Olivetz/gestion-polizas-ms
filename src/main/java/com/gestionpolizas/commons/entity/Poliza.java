package com.gestionpolizas.commons.entity;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Representa una póliza de seguro de arrendamiento de inmuebles.
 *
 * <p>La prima se deriva como {@code canonMensual * numeroMesesVigencia}. En la renovación, el canon
 * se ajusta según el IPC y la prima se recalcula.</p>
 */
@Entity
@Table(name = "poliza")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Poliza {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private TipoPoliza tipo;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private EstadoPoliza estado;

    @Column(name = "tomador_id", length = 50)
    private String tomadorId;

    @Column(name = "asegurado_id", length = 50)
    private String aseguradoId;

    @Column(name = "beneficiario_id", length = 50)
    private String beneficiarioId;

    @Column(name = "fecha_inicio_vigencia", nullable = false)
    private LocalDate fechaInicioVigencia;

    @Column(name = "fecha_fin_vigencia", nullable = false)
    private LocalDate fechaFinVigencia;

    @Column(name = "canon_mensual", nullable = false, precision = 15, scale = 2)
    private BigDecimal canonMensual;

    @Column(name = "numero_meses_vigencia", nullable = false)
    private Integer numeroMesesVigencia;

    @Column(name = "prima", nullable = false, precision = 15, scale = 2)
    private BigDecimal prima;

    /** Porcentaje de IPC aplicado en la última renovación (null si nunca se ha renovado). */
    @Column(name = "ipc_aplicado", precision = 6, scale = 3)
    private BigDecimal ipcAplicado;

    @Column(name = "fecha_creacion", nullable = false)
    private LocalDateTime fechaCreacion;

    @Column(name = "fecha_actualizacion")
    private LocalDateTime fechaActualizacion;

    /**
     * Riesgos asociados a la póliza. La cascada y el {@code orphanRemoval} permiten que la
     * cancelación en cascada y la gestión del ciclo de vida se mantengan consistentes.
     */
    @OneToMany(mappedBy = "poliza", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @Builder.Default
    private List<Riesgo> riesgos = new ArrayList<>();
}
