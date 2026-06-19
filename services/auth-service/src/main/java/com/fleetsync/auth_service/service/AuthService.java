package com.fleetsync.auth_service.service;

import com.fleetsync.auth_service.dto.AuthResponse;
import com.fleetsync.auth_service.dto.LoginRequest;
import com.fleetsync.auth_service.dto.RegisterRequest;
import com.fleetsync.auth_service.entity.User;
import com.fleetsync.auth_service.exception.InvalidCredentialsException;
import com.fleetsync.auth_service.exception.InvalidTokenException;
import com.fleetsync.auth_service.exception.UserAlreadyExistsException;
import com.fleetsync.auth_service.exception.UserNotFoundException;
import com.fleetsync.auth_service.repository.UserRepository;
import com.fleetsync.auth_service.security.JwtUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * Service for handling authentication operations
 * Handles user registration, login, token refresh, and logout
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {

    private final UserRepository repository;
    private final RedisTemplate<Object, Object> redisTemplate;
    private final JwtUtils jwt;
    private final PasswordEncoder passwordEncoder;

    /**
     * Registers a new user
     *
     * @param request Registration request with user details
     * @return Authentication response with tokens
     * @throws UserAlreadyExistsException if email already exists
     */
    public AuthResponse register(RegisterRequest request) {
        log.info("Attempting to register user with email: {}", request.getEmail());

        if (repository.existsByEmail(request.getEmail())) {
            log.warn("Registration failed: Email already exists - {}", request.getEmail());
            throw new UserAlreadyExistsException("Email already exists: " + request.getEmail());
        }

        String hashedPassword = passwordEncoder.encode(request.getPassword());

        User user = User.builder()
                .name(request.getName())
                .email(request.getEmail())
                .password(hashedPassword)
                .role(request.getRole())
                .build();

        repository.save(user);
        log.info("User registered successfully: {}", request.getEmail());
        return generateToken(user);
    }

    /**
     * Authenticates a user and returns tokens
     *
     * @param request Login request with email and password
     * @return Authentication response with tokens
     * @throws UserNotFoundException   if user not found
     * @throws InvalidCredentialsException if password is incorrect
     */
    public AuthResponse login(LoginRequest request) {
        log.info("Attempting login for user: {}", request.getEmail());

        User user = repository.findByEmail(request.getEmail())
                .orElseThrow(() -> {
                    log.warn("Login failed: User not found - {}", request.getEmail());
                    return new UserNotFoundException("User not found with email: " + request.getEmail());
                });

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            log.warn("Login failed: Invalid credentials for user - {}", request.getEmail());
            throw new InvalidCredentialsException("Invalid email or password");
        }

        log.info("User logged in successfully: {}", request.getEmail());
        return generateToken(user);
    }

    /**
     * Refreshes access token using refresh token
     *
     * @param refreshToken Refresh token from Redis
     * @return New authentication response with new tokens
     * @throws InvalidTokenException if refresh token is invalid or expired
     * @throws UserNotFoundException if user not found
     */
    public AuthResponse refresh(String refreshToken) {
        log.debug("Attempting to refresh token");

        Object emailObj = redisTemplate.opsForValue().get("refresh:" + refreshToken);
        // FIXED: Check for null before calling toString() to prevent NullPointerException
        if (emailObj == null) {
            log.warn("Token refresh failed: Invalid or expired refresh token");
            throw new InvalidTokenException("Invalid or expired refresh token");
        }

        String email = emailObj.toString();
        User user = repository.findByEmail(email)
                .orElseThrow(() -> {
                    log.warn("Token refresh failed: User not found - {}", email);
                    return new UserNotFoundException("User not found with email: " + email);
                });

        // Token rotation: delete old, issue new
        redisTemplate.delete("refresh:" + refreshToken);
        log.info("Token refreshed successfully for user: {}", email);
        return generateToken(user);
    }

    /**
     * Logs out a user by invalidating their refresh token
     *
     * @param refreshToken Refresh token to invalidate
     */
    public void logout(String refreshToken) {
        log.info("User logging out");
        redisTemplate.delete("refresh:" + refreshToken);
        log.info("User logged out successfully");
    }

    /**
     * Generates access and refresh tokens for a user
     * FIXED: Now passes user.getRole().name() instead of user.getPassword() to JWT
     *
     * @param user User entity
     * @return Authentication response with tokens
     */
    private AuthResponse generateToken(User user) {
        // FIXED: Pass role.name() instead of hashed password
        String accessToken = jwt.generateAccessToken(user.getEmail(), user.getRole().name());
        String refreshToken = UUID.randomUUID().toString();

        // Store refresh token in Redis with 7-day expiration
        redisTemplate.opsForValue().set("refresh:" + refreshToken, user.getEmail(), 7, TimeUnit.DAYS);

        return AuthResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .tokenType("bearer")
                .name(user.getName())
                .role(user.getRole())
                .build();
    }
}
