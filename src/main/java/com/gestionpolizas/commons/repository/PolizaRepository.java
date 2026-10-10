package com.gestionpolizas.commons.repository;

import java.util.List;

import com.gestionpolizas.commons.entity.EstadoPoliza;
import com.gestionpolizas.commons.entity.Poliza;
import com.gestionpolizas.commons.entity.TipoPoliza;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repositorio JPA para la entidad {@link Poliza}. Spring Data genera las consultas a partir de
 * los nombres de los metodos (derived queries), evitando concatenacion de SQL (OWASP A05).
 */
@Repository
public interface PolizaRepository extends JpaRepository<Poliza, Long> {

    List<Poliza> findByTipoAndEstado(TipoPoliza tipo, EstadoPoliza estado);

    List<Poliza> findByTipo(TipoPoliza tipo);

    List<Poliza> findByEstado(EstadoPoliza estado);
}
