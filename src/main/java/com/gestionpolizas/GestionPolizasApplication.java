package com.gestionpolizas;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Punto de entrada del microservicio de Gestión de Pólizas.
 *
 * <p>Expone una API REST para la gestión de pólizas de arrendamiento (individuales y colectivas)
 * y sus riesgos asociados, siguiendo una organización por caso de uso (vertical slicing) con
 * capas controller / service / repository.</p>
 */
@SpringBootApplication
public class GestionPolizasApplication {

    public static void main(String[] args) {
        SpringApplication.run(GestionPolizasApplication.class, args);
    }
}
