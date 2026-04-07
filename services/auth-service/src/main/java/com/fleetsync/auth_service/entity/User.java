package com.fleetsync.auth_service.entity;

import com.fleetsync.auth_service.enums.Role;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.validation.annotation.Validated;

import java.time.LocalDateTime;

@Entity
@Table(name = "users")
@Data
@Validated
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Column(nullable = false)
    private String name;

    @NotBlank
    @Column(nullable = false, unique = true)
    private String email;

    @NotBlank
    @Column(nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

}