package com.actividad2.cloud_gateway.dto;

public class TokenValidationRequest {

    private String opaqueToken;

    public TokenValidationRequest(String opaqueToken) {
        this.opaqueToken = opaqueToken;
    }

    public String getOpaqueToken() {
        return opaqueToken;
    }
}
