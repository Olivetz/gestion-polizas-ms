package com.gestionpolizas.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuracion de la documentacion OpenAPI / Swagger UI del microservicio.
 *
 * <p>Registra el esquema de seguridad de la api-key para que Swagger UI muestre el boton
 * "Authorize" y envie el header {@code api-key} en las peticiones de prueba.</p>
 */
@Configuration
public class OpenApiConfig {

    private static final String API_KEY_SCHEME = "apiKey";
    private static final String API_KEY_HEADER = "api-key";

    @Bean
    public OpenAPI gestionPolizasOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("API Gestion de Polizas")
                        .description("Microservicio para la gestion de polizas de arrendamiento "
                                + "(individuales y colectivas) y sus riesgos asociados.")
                        .version("1.0.0")
                        .contact(new Contact().name("Olivet Chona - Desarrollador TI"))
                        .license(new License().name("Apache 2.0")
                                .url("http://www.apache.org/licenses/LICENSE-2.0.html")))
                // Define el esquema de seguridad: API key en el header 'api-key'.
                .components(new Components().addSecuritySchemes(API_KEY_SCHEME,
                        new SecurityScheme()
                                .type(SecurityScheme.Type.APIKEY)
                                .in(SecurityScheme.In.HEADER)
                                .name(API_KEY_HEADER)
                                .description("Credencial de acceso. Valor para el ejercicio: 123456")))
                // Aplica el esquema a todos los endpoints (boton "Authorize" global).
                .addSecurityItem(new SecurityRequirement().addList(API_KEY_SCHEME));
    }
}
