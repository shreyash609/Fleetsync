package com.fleetsync.auth_service.dto;

import com.fleetsync.auth_service.enums.Role;
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
    private Role role;
}
