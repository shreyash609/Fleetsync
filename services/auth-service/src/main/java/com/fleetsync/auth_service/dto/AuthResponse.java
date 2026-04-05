package com.fleetsync.auth_service.dto;

import lombok.Builder;
import lombok.Data;
import org.springframework.validation.annotation.Validated;

@Data
@Builder
public class AuthResponse {

    private String accessToken;
    private String refreshToken;
    private String tokenType;
    private String name;
    private String role;//will later make it enum type
}
