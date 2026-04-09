package com.fleetsync.auth_service.service;

import com.fleetsync.auth_service.dto.AuthResponse;
import com.fleetsync.auth_service.dto.RegisterRequest;
import com.fleetsync.auth_service.entity.User;
import com.fleetsync.auth_service.repository.UserRepository;
import com.fleetsync.auth_service.security.JwtUtils;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private UserRepository repository;
    private JwtUtils jwt;
    private final PasswordEncoder passwordEncoder;

    //autowiring
    public AuthService(UserRepository repository,PasswordEncoder passwordEncoder){
        this.repository=repository;
        this.passwordEncoder=passwordEncoder;
    }

    public AuthResponse register(RegisterRequest request) throws Exception {

        //1.checking email is already exists or not
        boolean isEmailExist=repository.existsByEmail(request.getEmail());

        if(isEmailExist){
             throw new Exception("Email already exists");
        }

       //2.Hashing the password using the encoder
        String hashedPassword=passwordEncoder.encode(request.getPassword());

        //3. saving the user
        User user=new User();
        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setPassword(hashedPassword);
        user.setRole(request.getRole());

       User userCreated= repository.save(user);

      //4.Generating the Access token
        String tokens=jwt.generateAccessToken(userCreated.getEmail(), String.valueOf(user.getRole()));

        return AuthResponse.builder()
                .accessToken(tokens)
                .refreshToken(null)
                .tokenType("bearer")
                .name(userCreated.getName())
                .role(userCreated.getRole())
                .build();
    }
}
