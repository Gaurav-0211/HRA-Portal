package com.hra.hra.service;

import com.hra.hra.entity.RefreshToken;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;

import java.time.Instant;
import java.util.Optional;

public interface AuthService {

    // core refresh token operations
    RefreshToken create(String username, String token, Instant expiry);

    void deleteToken(String token);

    Optional<RefreshToken> findByToken(String token);

    ResponseEntity<?> login(String email, String password, HttpServletResponse response);

    ResponseEntity<?> refreshToken(HttpServletRequest request, HttpServletResponse response);

    ResponseEntity<?> logout(HttpServletRequest request, HttpServletResponse response);
}
