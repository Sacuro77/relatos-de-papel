package com.actividad3.users_service.dto;

import java.time.Instant;
import java.util.List;

public class OpaqueTokenData {

    private String accessToken;
    private Instant expiresAt;
    private String subject;
    private String email;
    private List<String> roles;

    public OpaqueTokenData() {
    }

    public OpaqueTokenData(String accessToken, Instant expiresAt, String subject, String email, List<String> roles) {
        this.accessToken = accessToken;
        this.expiresAt = expiresAt;
        this.subject = subject;
        this.email = email;
        this.roles = roles;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public Instant getExpiresAt() {
        return expiresAt;
    }

    public void setExpiresAt(Instant expiresAt) {
        this.expiresAt = expiresAt;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public List<String> getRoles() {
        return roles;
    }

    public void setRoles(List<String> roles) {
        this.roles = roles;
    }
}
