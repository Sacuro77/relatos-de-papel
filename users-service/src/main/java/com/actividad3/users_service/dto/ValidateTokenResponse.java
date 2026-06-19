package com.actividad3.users_service.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.time.Instant;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class ValidateTokenResponse {

    private boolean active;
    private String accessToken;
    private Instant expiresAt;
    private String subject;
    private String email;
    private List<String> roles;

    public ValidateTokenResponse() {
    }

    public ValidateTokenResponse(boolean active, String accessToken, Instant expiresAt, String subject, String email, List<String> roles) {
        this.active = active;
        this.accessToken = accessToken;
        this.expiresAt = expiresAt;
        this.subject = subject;
        this.email = email;
        this.roles = roles;
    }

    public static ValidateTokenResponse inactive() {
        return new ValidateTokenResponse(false, null, null, null, null, null);
    }

    public boolean isActive() {
        return active;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public Instant getExpiresAt() {
        return expiresAt;
    }

    public String getSubject() {
        return subject;
    }

    public String getEmail() {
        return email;
    }

    public List<String> getRoles() {
        return roles;
    }
}
