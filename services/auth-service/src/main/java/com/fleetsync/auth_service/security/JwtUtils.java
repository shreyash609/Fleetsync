package com.fleetsync.auth_service.security;

import com.fleetsync.auth_service.enums.Role;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.crypto.SecretKey;
import java.security.Key;
import java.util.Date;

/**
 * JWT utility class for token generation and validation
 */
@Component
@Slf4j
public class JwtUtils {

    @Value("${jwt.secret}")
    private String secretKey;

    @Value("${jwt.expiration}")
    private Long expiration;

    /**
     * Validates JWT configuration during application startup
     */
    @PostConstruct
    public void validateConfig() {
        if (secretKey == null || secretKey.isEmpty()) {
            throw new IllegalStateException("jwt.secret is not configured. Please set jwt.secret in application.properties");
        }
        if (secretKey.length() < 32) {
            log.warn("JWT secret key is less than 32 characters. This may cause security issues.");
        }
        if (expiration == null || expiration <= 0) {
            throw new IllegalStateException("jwt.expiration must be a positive number. Please set jwt.expiration in application.properties");
        }
        log.info("JWT configuration validated successfully");
    }

    /**
     * Gets the signing key for JWT operations
     * Fixed typo: was getSingingKey, now getSigningKey
     */
    private Key getSigningKey() {
        return Keys.hmacShaKeyFor(secretKey.getBytes());
    }

    /**
     * Generates an access token with user email and role
     * FIXED: Now passes user role instead of hashed password
     *
     * @param email User email
     * @param role  User role (should be role.name(), not password)
     * @return JWT access token
     */
    public String generateAccessToken(String email, String role) {
        log.debug("Generating access token for user: {}", email);
        return Jwts.builder()
                .subject(email)
                .claim("role", role)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    /**
     * Extracts all claims from a JWT token
     */
    private Claims extractAllClaims(String token) {
        return Jwts.parser()
                .verifyWith((SecretKey) getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    /**
     * Validates if a token is valid and not expired
     */
    public boolean isTokenValid(String token) {
        try {
            extractAllClaims(token);
            return true;
        } catch (Exception e) {
            log.debug("Token validation failed: {}", e.getMessage());
            return false;
        }
    }

    /**
     * Extracts email from a JWT token
     */
    public String extractEmail(String token) {
        return extractAllClaims(token).getSubject();
    }

    /**
     * Extracts role from a JWT token
     */
    public String extractRole(String token) {
        return extractAllClaims(token).get("role", String.class);
    }
}
