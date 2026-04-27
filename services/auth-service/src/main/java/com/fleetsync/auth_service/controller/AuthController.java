package com.fleetsync.auth_service.controller;

import com.fleetsync.auth_service.dto.AuthResponse;
import com.fleetsync.auth_service.dto.LoginRequest;
import com.fleetsync.auth_service.dto.RegisterRequest;
import com.fleetsync.auth_service.security.JwtUtils;
import com.fleetsync.auth_service.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    //note : DI is handled using the constructor Injection and constructor we are handling using the Lombok

    private final AuthService authService;
    private final JwtUtils jwtUtil;

    //-----------Register------------------------------------
    @PostMapping("/register")
    public ResponseEntity<AuthResponse> authRegister(@Valid @RequestBody RegisterRequest registerRequest){
        return ResponseEntity.ok(authService.register(registerRequest));
    }

    //------------Login----------------------------------------
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest loginRequest){
        return ResponseEntity.ok(authService.login(loginRequest));
    }

    //-----------refresh----------------------------------------
    @PostMapping("/refresh")
    public ResponseEntity<AuthResponse> refresh(@RequestHeader("X-Refresh-Token:")String token ){
        return ResponseEntity.ok(authService.refresh(token));
    }

    //-----------logout----------------------------------------
    @PostMapping("/logout")
    public ResponseEntity<Void> logout(@RequestHeader("X-Refresh-Token:")String token){
        authService.logout(token);
        return ResponseEntity.noContent().build();
    }

    //-----------Validate----------------------------------------
    // Called by API Gateway to validate tokens from other services (remaining testing of this method )
    @GetMapping("/validate")
    public ResponseEntity<Map<String, String>> validate(@RequestHeader("Authorization") String bearerToken) {

        String token = bearerToken.replace("Bearer ", "");
        if (!jwtUtil.isTokenValid(token)) {
            return ResponseEntity.status(401).build();
        }
        return ResponseEntity.ok(Map.of(
                "email", jwtUtil.extractEmail(token),
                "role",  jwtUtil.extractRole(token)
        ));
    }
}
