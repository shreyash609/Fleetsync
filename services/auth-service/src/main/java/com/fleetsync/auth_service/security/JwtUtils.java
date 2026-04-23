package com.fleetsync.auth_service.security;

import com.fleetsync.auth_service.enums.Role;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
@Component
public class JwtUtils {

    @Value("${jwt.secret}")
    private String secretKey;

    @Value("${jwt.expiration}")
    private Long expiration;

    private Key getSingingKey(){
        return Keys.hmacShaKeyFor(secretKey.getBytes());
    }

    public String generateAccessToken(String email, String role){
        return Jwts.builder().subject(email)
                .claim("role", role).issuedAt(new Date()).expiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(getSingingKey(), SignatureAlgorithm.HS256)
                .compact();

    }

    public String extractEmail(String token){
        return extractAllClaims(token).getSubject();
    }

    public String extractRole(String token){
        return extractAllClaims(token).get("role",String.class);
    }

    public boolean isTokenValid(String token){
         try{
             extractAllClaims(token);
             return true;
         }catch (Exception e){
             return false;
         }
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parser()
                .setSigningKey(getSingingKey())
                .build().parseSignedClaims(token).getPayload();
    }
}
