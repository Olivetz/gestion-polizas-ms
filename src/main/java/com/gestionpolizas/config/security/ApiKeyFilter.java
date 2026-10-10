package com.gestionpolizas.config.security;

import java.io.IOException;
import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gestionpolizas.commons.dto.ApiResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * Filtro transversal que exige el header {@code api-key} en todas las peticiones a la API,
 * salvo rutas públicas (documentación OpenAPI y health). El valor esperado se resuelve desde
 * configuración ({@code security.api-key}) y nunca se escribe en logs (OWASP A07/A09).
 *
 * <p>En un entorno productivo este microservicio iría detrás de un API Gateway que valida un JWT
 * del IdP institucional; este filtro cumple el requisito de "seguridad mínima" del ejercicio
 * (header api-key) aportando además defensa en profundidad.</p>
 */
@Component
public class ApiKeyFilter extends OncePerRequestFilter {

    private static final Logger log = LoggerFactory.getLogger(ApiKeyFilter.class);
    private static final String HEADER_API_KEY = "api-key";

    private final String apiKeyEsperada;
    private final ObjectMapper objectMapper;
    private final AntPathMatcher pathMatcher = new AntPathMatcher();

    /** Rutas que no requieren api-key (documentación y verificación de salud). */
    private static final List<String> RUTAS_PUBLICAS = List.of(
            "/v3/api-docs/**",
            "/swagger-ui/**",
            "/swagger-ui.html",
            "/api/v1/actuator/**",
            "/actuator/**",
            "/h2-console/**"
    );

    public ApiKeyFilter(@Value("${security.api-key}") String apiKeyEsperada,
                        ObjectMapper objectMapper) {
        this.apiKeyEsperada = apiKeyEsperada;
        this.objectMapper = objectMapper;
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String ruta = request.getRequestURI();
        if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
            return true;
        }
        return RUTAS_PUBLICAS.stream().anyMatch(patron -> pathMatcher.match(patron, ruta));
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        String apiKeyRecibida = request.getHeader(HEADER_API_KEY);

        if (apiKeyRecibida == null || !apiKeyRecibida.equals(apiKeyEsperada)) {
            // No se registra el valor recibido para no filtrar credenciales en logs.
            log.warn("Petición rechazada por api-key inválida o ausente - ruta: {}", request.getRequestURI());
            escribirNoAutorizado(response);
            return;
        }

        filterChain.doFilter(request, response);
    }

    private void escribirNoAutorizado(HttpServletResponse response) throws IOException {
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding("UTF-8");
        ApiResponse<Void> cuerpo = ApiResponse.error("NO_AUTORIZADO",
                "Credencial de acceso inválida o ausente.");
        response.getWriter().write(objectMapper.writeValueAsString(cuerpo));
    }
}
