package com.actividad2.cloud_gateway.client;

import com.actividad2.cloud_gateway.config.GatewaySecurityProperties;
import com.actividad2.cloud_gateway.dto.TokenValidationRequest;
import com.actividad2.cloud_gateway.dto.TokenValidationResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;

import java.net.URI;

@Component
public class AuthValidationClient {

    private static final Logger log = LoggerFactory.getLogger(AuthValidationClient.class);

    private final LoadBalancerClient loadBalancerClient;
    private final RestClient restClient;
    private final GatewaySecurityProperties properties;

    public AuthValidationClient(
            LoadBalancerClient loadBalancerClient,
            RestClient restClient,
            GatewaySecurityProperties properties
    ) {
        this.loadBalancerClient = loadBalancerClient;
        this.restClient = restClient;
        this.properties = properties;
    }

    public TokenValidationResponse validate(String opaqueToken) {
        ServiceInstance instance = loadBalancerClient.choose(properties.getValidationServiceId());
        if (instance == null) {
            log.warn("No hay instancias disponibles para {}", properties.getValidationServiceId());
            throw new AuthValidationException("users-service no disponible");
        }

        URI validationUri = instance.getUri().resolve(properties.getValidationPath());

        try {
            return restClient.post()
                    .uri(validationUri)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(new TokenValidationRequest(opaqueToken))
                    .retrieve()
                    .body(TokenValidationResponse.class);
        } catch (RestClientException ex) {
            log.warn("Fallo validando token opaco contra {}", validationUri, ex);
            throw new AuthValidationException("No se pudo validar el token opaco", ex);
        }
    }
}
