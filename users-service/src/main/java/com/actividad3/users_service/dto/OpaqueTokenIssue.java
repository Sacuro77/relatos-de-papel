package com.actividad3.users_service.dto;

public class OpaqueTokenIssue {

    private final String opaqueToken;
    private final long expiresIn;

    public OpaqueTokenIssue(String opaqueToken, long expiresIn) {
        this.opaqueToken = opaqueToken;
        this.expiresIn = expiresIn;
    }

    public String getOpaqueToken() {
        return opaqueToken;
    }

    public long getExpiresIn() {
        return expiresIn;
    }
}
