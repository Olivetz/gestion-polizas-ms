package com.gestionpolizas.config.security;

import java.io.IOException;

import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * Agrega headers HTTP de seguridad a todas las respuestas (OWASP A02): HSTS, CSP,
 * X-Content-Type-Options, X-Frame-Options, Referrer-Policy y Permissions-Policy.
 */
@Component
@Order(1)
public class SecurityHeadersFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        if ("OPTIONS".equalsIgnoreCase(httpRequest.getMethod())) {
            chain.doFilter(request, response);
            return;
        }

        // La consola H2 (solo desarrollo) se renderiza en frames; no se le aplican los headers
        // restrictivos para no romper su funcionamiento. El resto de rutas sí los recibe.
        if (httpRequest.getRequestURI().startsWith("/h2-console")) {
            chain.doFilter(request, response);
            return;
        }

        httpResponse.setHeader("Strict-Transport-Security", "max-age=31536000; includeSubDomains");
        httpResponse.setHeader("Content-Security-Policy",
                "default-src 'self'; frame-ancestors 'none'");
        httpResponse.setHeader("X-Content-Type-Options", "nosniff");
        httpResponse.setHeader("X-Frame-Options", "DENY");
        httpResponse.setHeader("Referrer-Policy", "strict-origin-when-cross-origin");
        httpResponse.setHeader("Permissions-Policy",
                "geolocation=(), camera=(), microphone=(), payment=(), usb=()");

        chain.doFilter(request, response);
    }
}
