package com.wordbridge.project.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;

@Component
public class JwtUtil {


    @Value("${jwt.secret}")
    private String secret;
    @Value("${jwt.expiration}")
    private Long expiration;

    @Value("${jwt.verification-expiration}")
    private Long verificationExpiration;

    @Value("${jwt.reset-expiration}")
    private Long resetExpiration;

    //Generate Token from email
    public String generateToken(Long userId, String email, String role) {
        return Jwts.builder()
                .subject(email)
                .claim("userId", userId)
                .claim("role", role)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(getKey())
                .compact();
    }


    //Generate Email Verification token small time token
    public String generateVerificationToken(String email) {
        return Jwts.builder().subject(email).claim("purpose", "EMAIL_VERIFICATION")
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + verificationExpiration))
                .signWith(getKey())
                .compact();
    }

    //Generate Password Reset Token
    public String generatePasswordResetToken(String email) {
        return Jwts.builder()
                .subject(email)
                .claim("purpose", "PASSWORD_RESET")
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + resetExpiration))
                .signWith(getKey())
                .compact();
    }


    //Extract Purpose from token
    public String extractPurpose(String token) {
        return (String) getClaims(token).get("purpose");
    }

    //Is Token Valid For Expected Purpose?
    public boolean isValidForPurpose(String token, String expectedPurpose) {
        try {
            Claims claims = getClaims(token);
            return expectedPurpose.equals(claims.get("purpose"));
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }


    //Extract email from token
    public String extractEmail(String token) {
        return getClaims(token).getSubject();
    }

    //Extract User id from token
    public Long extractUserId(String token) {
        return getClaims(token).get("userId", Long.class);
    }

    //Extract role claim from token
    public String extractRole(String token) {
        return (String) getClaims(token).get("role");
    }

    //Validate Token Check signature + expiry
    public boolean isValid(String token) {
        try {
            getClaims(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }


    //Helper Methods

    private SecretKey getKey() {
        return Keys.hmacShaKeyFor(secret.getBytes());
    }

    private Claims getClaims(String token) {
        return Jwts.parser()
                .verifyWith(getKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }


}
