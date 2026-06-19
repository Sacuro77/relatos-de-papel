package com.actividad3.users_service.dto;

import jakarta.validation.constraints.NotBlank;

public class RefreshTokenRequest {

    @NotBlank(message = "El token opaco es obligatorio")
    private String opaqueToken;

    public String getOpaqueToken() {
        return opaqueToken;
    }

    public void setOpaqueToken(String opaqueToken) {
        this.opaqueToken = opaqueToken;
    }
}
