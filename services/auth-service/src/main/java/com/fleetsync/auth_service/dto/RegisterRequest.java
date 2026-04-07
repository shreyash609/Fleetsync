package com.fleetsync.auth_service.dto;

import com.fleetsync.auth_service.enums.Role;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.springframework.validation.annotation.Validated;

@Data
@Validated
public class RegisterRequest {

    @NotBlank
    private String name;

    @Email
    @NotBlank
    private String email;

    @NotBlank
    @Size(min=5,max=10)
    private String password;

    @Enumerated(EnumType.STRING)
    private Role role;
}
