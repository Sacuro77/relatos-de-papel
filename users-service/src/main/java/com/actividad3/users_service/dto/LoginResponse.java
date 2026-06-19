package com.actividad3.users_service.dto;

public class LoginResponse {

    private String tokenType;
    private String opaqueToken;
    private long expiresIn;

    public LoginResponse(String tokenType, String opaqueToken, long expiresIn) {
        this.tokenType = tokenType;
        this.opaqueToken = opaqueToken;
        this.expiresIn = expiresIn;
    }

    public String getTokenType() {
        return tokenType;
    }

    public String getOpaqueToken() {
        return opaqueToken;
    }

    public long getExpiresIn() {
        return expiresIn;
    }
}
