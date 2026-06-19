package com.actividad2.cloud_gateway.filter;

import com.actividad2.cloud_gateway.client.AuthValidationClient;
import com.actividad2.cloud_gateway.client.AuthValidationException;
import com.actividad2.cloud_gateway.config.GatewaySecurityProperties;
import com.actividad2.cloud_gateway.dto.TokenValidationResponse;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class PhantomTokenFilter extends OncePerRequestFilter {

    private static final Logger log = LoggerFactory.getLogger(PhantomTokenFilter.class);
    private static final String BEARER_PREFIX = "Bearer ";

    private final AuthValidationClient authValidationClient;
    private final GatewaySecurityProperties properties;
    private final AntPathMatcher pathMatcher = new AntPathMatcher();

    public PhantomTokenFilter(AuthValidationClient authValidationClient, GatewaySecurityProperties properties) {
        this.authValidationClient = authValidationClient;
        this.properties = properties;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String path = request.getRequestURI();
        String method = request.getMethod();

        if (isPublicRoute(method, path)) {
            filterChain.doFilter(request, response);
            return;
        }

        if (isBlockedExternalAuthRoute(path)) {
            log.warn("Intento externo de acceder a endpoint auth interno: {} {}", method, path);
            unauthorized(response, "Endpoint de autenticacion no publico");
            return;
        }

        if (!isProtectedRoute(path)) {
            filterChain.doFilter(request, response);
            return;
        }

        String opaqueToken = extractOpaqueToken(request);
        if (opaqueToken == null) {
            log.warn("Solicitud protegida sin token Bearer: {} {}", method, path);
            unauthorized(response, "Token opaco requerido");
            return;
        }

        TokenValidationResponse validation;
        try {
            validation = authValidationClient.validate(opaqueToken);
        } catch (AuthValidationException ex) {
            serviceUnavailable(response, "No se pudo validar el token opaco");
            return;
        }

        if (validation == null || !validation.isActive() || validation.getAccessToken() == null || validation.getAccessToken().isBlank()) {
            log.warn("Token opaco invalido para {} {}", method, path);
            unauthorized(response, "Token opaco invalido");
            return;
        }

        log.info("Token opaco validado para subject={} email={} ruta={}", validation.getSubject(), validation.getEmail(), path);
        HttpServletRequest wrappedRequest = new AccessTokenRequestWrapper(
                request,
                properties.getAccessTokenHeader(),
                validation.getAccessToken()
        );
        filterChain.doFilter(wrappedRequest, response);
    }

    private boolean isPublicRoute(String method, String path) {
        if (matchesAny(path, List.of("/actuator/**"))) {
            return true;
        }

        if (HttpMethod.POST.matches(method) && matchesAny(path, List.of(
                "/api/v1/auth/login",
                "/api/v1/auth/refresh"
        ))) {
            return true;
        }

        if (HttpMethod.GET.matches(method) && matchesAny(path, List.of(
                "/api/v1/books/**",
                "/catalogue/**",
                "/api/catalogue/search"
        ))) {
            return true;
        }

        return false;
    }

    private boolean isBlockedExternalAuthRoute(String path) {
        return matchesAny(path, List.of("/api/v1/auth/**"));
    }

    private boolean isProtectedRoute(String path) {
        return matchesAny(path, List.of(
                "/api/v1/orders/**",
                "/api/v1/users/profile",
                "/orders/**",
                "/api/orders/**"
        ));
    }

    private String extractOpaqueToken(HttpServletRequest request) {
        String authorization = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (authorization == null || !authorization.startsWith(BEARER_PREFIX)) {
            return null;
        }

        String token = authorization.substring(BEARER_PREFIX.length()).trim();
        return token.isBlank() ? null : token;
    }

    private boolean matchesAny(String path, List<String> patterns) {
        return patterns.stream().anyMatch(pattern -> pathMatcher.match(pattern, path));
    }

    private void unauthorized(HttpServletResponse response, String message) throws IOException {
        writeError(response, HttpStatus.UNAUTHORIZED, message);
    }

    private void serviceUnavailable(HttpServletResponse response, String message) throws IOException {
        writeError(response, HttpStatus.SERVICE_UNAVAILABLE, message);
    }

    private void writeError(HttpServletResponse response, HttpStatus status, String message) throws IOException {
        response.setStatus(status.value());
        response.setContentType("application/json");
        response.getWriter().write("""
                {"status":%d,"error":"%s","message":"%s"}""".formatted(
                status.value(),
                status.getReasonPhrase(),
                message
        ));
    }
}
