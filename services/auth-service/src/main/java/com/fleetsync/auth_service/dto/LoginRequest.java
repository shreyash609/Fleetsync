package com.fleetsync.auth_service.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import org.springframework.validation.annotation.Validated;

@Data
@Validated
public class LoginRequest {

    @Email
    @NotBlank
    private String email;

    @NotBlank
    private String password;
}
