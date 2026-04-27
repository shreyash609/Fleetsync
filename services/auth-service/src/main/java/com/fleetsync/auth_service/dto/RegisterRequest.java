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
public class RegisterRequest {

    @NotBlank(message = "name is required")
    private String name;

    @Email(message = "valid email required")
    @NotBlank
    private String email;

    @NotBlank(message = "Password must be at least 8 characters")
    @Size(min=5,max=10)
    private String password;

    @Enumerated(EnumType.STRING)
    private Role role;
}
