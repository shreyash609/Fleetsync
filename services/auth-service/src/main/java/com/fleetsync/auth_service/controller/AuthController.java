package com.fleetsync.auth_service.controller;

import com.fleetsync.auth_service.dto.AuthResponse;
import com.fleetsync.auth_service.dto.LoginRequest;
import com.fleetsync.auth_service.dto.RegisterRequest;
import com.fleetsync.auth_service.security.JwtUtils;
import com.fleetsync.auth_service.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * REST Controller for authentication endpoints
 * Handles user registration, login, token refresh, logout, and token validation
 */
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Slf4j
public class AuthController {

    // Note: Dependency injection is handled using constructor injection with Lombok's @RequiredArgsConstructor

    private final AuthService authService;
    private final JwtUtils jwtUtil;

    /**
     * Register a new user
     *
     * @param registerRequest User registration details
     * @return Response with authentication tokens
     */
    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@Valid @RequestBody RegisterRequest registerRequest) {
        log.info("Register request received for email: {}", registerRequest.getEmail());
        return ResponseEntity.ok(authService.register(registerRequest));
    }

    /**
     * Login user with email and password
     *
     * @param loginRequest User login credentials
     * @return Response with authentication tokens
     */
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest loginRequest) {
        log.info("Login request received for email: {}", loginRequest.getEmail());
        return ResponseEntity.ok(authService.login(loginRequest));
    }

    /**
     * Refresh access token using refresh token
     *
     * @param token Refresh token from request header
     * @return Response with new authentication tokens
     */
    @PostMapping("/refresh")
    public ResponseEntity<AuthResponse> refresh(@RequestHeader("X-Refresh-Token") String token) {
        log.debug("Token refresh request received");
        return ResponseEntity.ok(authService.refresh(token));
    }

    /**
     * Logout user by invalidating refresh token
     *
     * @param token Refresh token from request header
     * @return No content response
     */
    @PostMapping("/logout")
    public ResponseEntity<Void> logout(@RequestHeader("X-Refresh-Token") String token) {
        log.info("Logout request received");
        authService.logout(token);
        return ResponseEntity.noContent().build();
    }

    /**
     * Validate JWT token
     * Called by API Gateway to validate tokens from other services
     *
     * @param bearerToken Authorization header with Bearer token
     * @return Response containing email and role if token is valid
     */
    @GetMapping("/validate")
    public ResponseEntity<Map<String, String>> validate(@RequestHeader("Authorization") String bearerToken) {
        log.debug("Token validation request received");
        String token = bearerToken.replace("Bearer ", "");
        if (!jwtUtil.isTokenValid(token)) {
            log.warn("Token validation failed: Invalid token");
            return ResponseEntity.status(401).build();
        }
        log.debug("Token validation successful");
        return ResponseEntity.ok(Map.of(
                "email", jwtUtil.extractEmail(token),
                "role", jwtUtil.extractRole(token)
        ));
    }
}
