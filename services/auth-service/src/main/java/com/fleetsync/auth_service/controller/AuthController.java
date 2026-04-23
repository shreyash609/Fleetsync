package com.fleetsync.auth_service.controller;

import com.fleetsync.auth_service.dto.AuthResponse;
import com.fleetsync.auth_service.dto.LoginRequest;
import com.fleetsync.auth_service.dto.RegisterRequest;
import com.fleetsync.auth_service.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    //-----------Register------------------------------------
    @PostMapping("/register")
    public ResponseEntity<AuthResponse> authRegister(@Valid @RequestBody RegisterRequest registerRequest) throws Exception {

        AuthResponse Authresponse=authService.register(registerRequest);
        return ResponseEntity.ok(Authresponse);
    }

    //------------Login----------------------------------------
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest loginRequest) throws Exception {

       AuthResponse authResponse= authService.login(loginRequest);
        return ResponseEntity.ok(authResponse);
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
}
