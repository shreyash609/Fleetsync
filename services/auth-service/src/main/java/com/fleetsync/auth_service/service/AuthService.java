package com.fleetsync.auth_service.service;

import com.fleetsync.auth_service.dto.AuthResponse;
import com.fleetsync.auth_service.dto.LoginRequest;
import com.fleetsync.auth_service.dto.RegisterRequest;
import com.fleetsync.auth_service.entity.User;
import com.fleetsync.auth_service.repository.UserRepository;
import com.fleetsync.auth_service.security.JwtUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository repository;
    private final RedisTemplate<Object, Object> redisTemplate;
    private final JwtUtils jwt;
    private final PasswordEncoder passwordEncoder;

    //----------------Register--------------------------------------
    public AuthResponse register(RegisterRequest request) throws Exception {

        if(repository.existsByEmail(request.getEmail())){
            throw new Exception("Email already exists");
        }

        String hashedPassword=passwordEncoder.encode(request.getPassword());

         User user=User.builder()
                 .name(request.getName())
                 .email(request.getEmail())
                 .password(hashedPassword)
                 .role(request.getRole())
                 .build();

       User userCreated= repository.save(user);
       return generateToken(userCreated);
    }

    //-----------------Login----------------------------------------
    public AuthResponse login(LoginRequest request) throws Exception {

        User user=repository.findByEmail(request.getEmail())
                .orElseThrow(()->new RuntimeException("user not found"));

       if(!passwordEncoder.matches(request.getPassword(),user.getPassword())){
           throw new Exception("Invalid Credentials");
       }

        return generateToken(user);
    }

    //----------------------refresh---------------------------------------
    public AuthResponse refresh(String refreshToken){

        String email = redisTemplate.opsForValue().get("refresh:" + refreshToken).toString();
        if(email == null) throw new RuntimeException("Invalid or expired refresh token");

        User user=repository.findByEmail(email).orElseThrow(()-> new RuntimeException(("user not found")));

        // rotate — delete old, issue new
        redisTemplate.delete("refresh:" +refreshToken);
        return generateToken(user);
    }

    //--------------logout--------------------------------------
    public void logout(String refreshToken) {
        redisTemplate.delete("refresh:" + refreshToken);
    }

    //-------------------generateToken----------------------------
    private AuthResponse generateToken(User user){

        String accessToken= jwt.generateAccessToken(user.getEmail(), user.getPassword());
        String refreshToken =  UUID.randomUUID().toString();

        //storing refreshToken in redis
        redisTemplate.opsForValue().set("refresh:" + refreshToken,user.getEmail(), 7, TimeUnit.DAYS);

        return AuthResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .tokenType("bearer")
                .name(user.getName())
                .role(user.getRole())
                .build();
    }
}
