package com.fleetsync.auth_service.exception;

/**
 * Exception thrown when invalid credentials are provided during login
 */
public class InvalidCredentialsException extends RuntimeException {
    public InvalidCredentialsException(String message) {
        super(message);
    }
}
