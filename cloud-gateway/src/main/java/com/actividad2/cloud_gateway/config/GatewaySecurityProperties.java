package com.actividad2.cloud_gateway.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "gateway.security")
public class GatewaySecurityProperties {

    private String validationServiceId = "users-service";
    private String validationPath = "/api/v1/auth/validate";
    private String accessTokenHeader = "accessToken";

    public String getValidationServiceId() {
        return validationServiceId;
    }

    public void setValidationServiceId(String validationServiceId) {
        this.validationServiceId = validationServiceId;
    }

    public String getValidationPath() {
        return validationPath;
    }

    public void setValidationPath(String validationPath) {
        this.validationPath = validationPath;
    }

    public String getAccessTokenHeader() {
        return accessTokenHeader;
    }

    public void setAccessTokenHeader(String accessTokenHeader) {
        this.accessTokenHeader = accessTokenHeader;
    }
}
