package com.hra.hra.controller;

import com.hra.hra.service.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
@Slf4j
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    // POST request to auth login to generate token
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> body, HttpServletResponse response) {
        log.info("Auth login in auth controller");
        return this.authService.login(body.get("email"), body.get("password"), response);

    }

    // POST request to refresh token when token expired - auto called
    @PostMapping("/refresh")
    public ResponseEntity<?> refreshToken(HttpServletRequest request, HttpServletResponse response) {
        log.info("Auth Refresh in auth controller");
        return this.authService.refreshToken(request, response);
    }

    // POST request to logout employee
    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpServletRequest request, HttpServletResponse response) {
        log.info("Auth logout in auth controller");
        return this.authService.logout(request, response);
    }
}
