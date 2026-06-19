package com.actividad3.users_service.service;

import com.actividad3.users_service.dto.OpaqueTokenData;
import com.actividad3.users_service.dto.OpaqueTokenIssue;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.time.Duration;
import java.time.Instant;
import java.util.Base64;

@Service
public class OpaqueTokenService {

    private final StringRedisTemplate redisTemplate;
    private final ObjectMapper objectMapper;
    private final String tokenPrefix;
    private final SecureRandom secureRandom = new SecureRandom();

    public OpaqueTokenService(
            StringRedisTemplate redisTemplate,
            ObjectMapper objectMapper,
            @Value("${app.opaque-token.prefix}") String tokenPrefix
    ) {
        this.redisTemplate = redisTemplate;
        this.objectMapper = objectMapper;
        this.tokenPrefix = tokenPrefix;
    }

    public OpaqueTokenIssue store(OpaqueTokenData data) {
        String opaqueToken = generateOpaqueToken();
        long expiresIn = Math.max(1, Duration.between(Instant.now(), data.getExpiresAt()).toSeconds());

        try {
            redisTemplate.opsForValue().set(redisKey(opaqueToken), objectMapper.writeValueAsString(data), Duration.ofSeconds(expiresIn));
        } catch (JsonProcessingException ex) {
            throw new IllegalStateException("No se pudo serializar el token opaco", ex);
        }

        return new OpaqueTokenIssue(opaqueToken, expiresIn);
    }

    public OpaqueTokenData find(String opaqueToken) {
        String value = redisTemplate.opsForValue().get(redisKey(opaqueToken));
        if (value == null || value.isBlank()) {
            return null;
        }

        try {
            return objectMapper.readValue(value, OpaqueTokenData.class);
        } catch (JsonProcessingException ex) {
            delete(opaqueToken);
            return null;
        }
    }

    public void delete(String opaqueToken) {
        redisTemplate.delete(redisKey(opaqueToken));
    }

    private String redisKey(String opaqueToken) {
        return tokenPrefix + opaqueToken;
    }

    private String generateOpaqueToken() {
        byte[] bytes = new byte[32];
        secureRandom.nextBytes(bytes);
        return "rpd_" + Base64.getUrlEncoder().withoutPadding().encodeToString(bytes);
    }
}
