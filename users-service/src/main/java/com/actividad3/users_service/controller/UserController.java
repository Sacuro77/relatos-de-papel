package com.actividad3.users_service.controller;

import com.actividad3.users_service.dto.ProfileResponse;
import com.actividad3.users_service.service.AuthService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {

    private final AuthService authService;

    public UserController(AuthService authService) {
        this.authService = authService;
    }

    @GetMapping("/profile")
    public ProfileResponse profile(@RequestHeader("accessToken") String accessToken) {
        return authService.profile(accessToken);
    }
}
