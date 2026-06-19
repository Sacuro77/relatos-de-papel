package com.actividad3.users_service.service;

import com.actividad3.users_service.dto.LoginRequest;
import com.actividad3.users_service.dto.LoginResponse;
import com.actividad3.users_service.dto.OpaqueTokenData;
import com.actividad3.users_service.dto.OpaqueTokenIssue;
import com.actividad3.users_service.dto.ProfileResponse;
import com.actividad3.users_service.dto.RefreshTokenRequest;
import com.actividad3.users_service.dto.ValidateTokenRequest;
import com.actividad3.users_service.dto.ValidateTokenResponse;
import com.actividad3.users_service.entity.AppUser;
import com.actividad3.users_service.exception.InvalidCredentialsException;
import com.actividad3.users_service.exception.InvalidTokenException;
import com.actividad3.users_service.repository.UserRepository;
import io.jsonwebtoken.Claims;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final OpaqueTokenService opaqueTokenService;

    public AuthService(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder,
            JwtService jwtService,
            OpaqueTokenService opaqueTokenService
    ) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.opaqueTokenService = opaqueTokenService;
    }

    public LoginResponse login(LoginRequest request) {
        AppUser user = userRepository.findByEmail(request.getEmail())
                .filter(AppUser::getEnabled)
                .orElseThrow(InvalidCredentialsException::new);

        if (!passwordEncoder.matches(request.getPassword(), user.getPasswordHash())) {
            throw new InvalidCredentialsException();
        }

        return issueToken(user);
    }

    public ValidateTokenResponse validate(ValidateTokenRequest request) {
        OpaqueTokenData data = opaqueTokenService.find(request.getOpaqueToken());
        if (data == null) {
            return ValidateTokenResponse.inactive();
        }

        try {
            jwtService.parseAndValidate(data.getAccessToken());
        } catch (InvalidTokenException ex) {
            opaqueTokenService.delete(request.getOpaqueToken());
            return ValidateTokenResponse.inactive();
        }

        if (data.getExpiresAt() == null || data.getExpiresAt().isBefore(Instant.now())) {
            opaqueTokenService.delete(request.getOpaqueToken());
            return ValidateTokenResponse.inactive();
        }

        return new ValidateTokenResponse(
                true,
                data.getAccessToken(),
                data.getExpiresAt(),
                data.getSubject(),
                data.getEmail(),
                data.getRoles()
        );
    }

    public LoginResponse refresh(RefreshTokenRequest request) {
        OpaqueTokenData data = opaqueTokenService.find(request.getOpaqueToken());
        if (data == null) {
            throw new InvalidTokenException("Token opaco invalido");
        }

        jwtService.parseAndValidate(data.getAccessToken());
        opaqueTokenService.delete(request.getOpaqueToken());

        AppUser user = userRepository.findById(Long.valueOf(data.getSubject()))
                .filter(AppUser::getEnabled)
                .orElseThrow(() -> new InvalidTokenException("Usuario no disponible"));

        return issueToken(user);
    }

    public ProfileResponse profile(String accessToken) {
        Claims claims = jwtService.parseAndValidate(accessToken);
        Long userId = Long.valueOf(claims.getSubject());

        AppUser user = userRepository.findById(userId)
                .filter(AppUser::getEnabled)
                .orElseThrow(() -> new InvalidTokenException("Usuario no disponible"));

        return new ProfileResponse(
                user.getId(),
                user.getEmail(),
                user.getFullName(),
                List.copyOf(user.getRoles())
        );
    }

    private LoginResponse issueToken(AppUser user) {
        String accessToken = jwtService.generateAccessToken(user);
        Instant expiresAt = jwtService.getExpiration(accessToken);

        OpaqueTokenData data = new OpaqueTokenData(
                accessToken,
                expiresAt,
                user.getId().toString(),
                user.getEmail(),
                List.copyOf(user.getRoles())
        );

        OpaqueTokenIssue issued = opaqueTokenService.store(data);
        return new LoginResponse("Bearer", issued.getOpaqueToken(), issued.getExpiresIn());
    }
}
