package com.fleetsync.auth_service.exception;

/**
 * Exception thrown when refresh token is invalid or expired
 */
public class InvalidTokenException extends RuntimeException {
    public InvalidTokenException(String message) {
        super(message);
    }
}
