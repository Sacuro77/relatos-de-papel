package com.actividad2.cloud_gateway.client;

public class AuthValidationException extends RuntimeException {

    public AuthValidationException(String message) {
        super(message);
    }

    public AuthValidationException(String message, Throwable cause) {
        super(message, cause);
    }
}
