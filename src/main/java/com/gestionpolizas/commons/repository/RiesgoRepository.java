package com.gestionpolizas.commons.repository;

import java.util.List;

import com.gestionpolizas.commons.entity.EstadoRiesgo;
import com.gestionpolizas.commons.entity.Riesgo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repositorio JPA para la entidad {@link Riesgo}.
 */
@Repository
public interface RiesgoRepository extends JpaRepository<Riesgo, Long> {

    List<Riesgo> findByPolizaId(Long polizaId);

    long countByPolizaIdAndEstado(Long polizaId, EstadoRiesgo estado);
}
