package com.actividad3.users_service.dto;

import java.util.List;

public class ProfileResponse {

    private Long id;
    private String email;
    private String fullName;
    private List<String> roles;

    public ProfileResponse(Long id, String email, String fullName, List<String> roles) {
        this.id = id;
        this.email = email;
        this.fullName = fullName;
        this.roles = roles;
    }

    public Long getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String getFullName() {
        return fullName;
    }

    public List<String> getRoles() {
        return roles;
    }
}
