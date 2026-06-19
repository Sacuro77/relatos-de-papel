package com.actividad2.orders_service.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

@Service
public class InternalJwtService {

    private final String secret;
    private final String issuer;

    public InternalJwtService(
            @Value("${app.jwt.secret}") String secret,
            @Value("${app.jwt.issuer}") String issuer
    ) {
        this.secret = secret;
        this.issuer = issuer;
    }

    public AuthenticatedUser authenticate(String accessToken) {
        if (accessToken == null || accessToken.isBlank()) {
            throw new AuthenticationException("Header accessToken requerido");
        }

        Claims claims;
        try {
            claims = Jwts.parser()
                    .verifyWith(signingKey())
                    .requireIssuer(issuer)
                    .build()
                    .parseSignedClaims(accessToken)
                    .getPayload();
        } catch (JwtException | IllegalArgumentException ex) {
            throw new AuthenticationException("JWT interno invalido o expirado");
        }

        String subject = claims.getSubject();
        String email = claims.get("email", String.class);
        List<String> roles = extractRoles(claims.get("roles"));

        if (subject == null || subject.isBlank()) {
            throw new AuthenticationException("JWT interno sin subject");
        }

        return new AuthenticatedUser(subject, email, roles);
    }

    private List<String> extractRoles(Object rolesClaim) {
        if (rolesClaim instanceof List<?> values) {
            List<String> roles = new ArrayList<>();
            for (Object value : values) {
                if (value != null) {
                    roles.add(value.toString());
                }
            }
            return roles;
        }

        return List.of();
    }

    private SecretKey signingKey() {
        return Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    }
}
