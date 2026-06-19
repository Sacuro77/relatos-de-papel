package com.actividad2.orders_service.security;

import java.util.List;

public class AuthenticatedUser {

    private final String userId;
    private final String email;
    private final List<String> roles;

    public AuthenticatedUser(String userId, String email, List<String> roles) {
        this.userId = userId;
        this.email = email;
        this.roles = roles;
    }

    public String getUserId() {
        return userId;
    }

    public String getEmail() {
        return email;
    }

    public List<String> getRoles() {
        return roles;
    }
}
