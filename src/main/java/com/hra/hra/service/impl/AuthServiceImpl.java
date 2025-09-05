package com.hra.hra.service.impl;

import com.hra.hra.config.AppConstants;
import com.hra.hra.config.CustomUserDetailService;
import com.hra.hra.entity.Employee;
import com.hra.hra.entity.RefreshToken;
import com.hra.hra.repository.EmployeeRepository;
import com.hra.hra.repository.RefreshTokenRepository;
import com.hra.hra.security.JwtTokenHelper;
import com.hra.hra.service.AuthService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Date;
import java.util.Map;
import java.util.Optional;
@Slf4j
@Service
public class AuthServiceImpl implements AuthService {

    @Autowired
    private RefreshTokenRepository refreshTokenRepository;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private CustomUserDetailService customUserDetailService;

    @Autowired
    private JwtTokenHelper jwtTokenHelper;

    @Autowired
    private EmployeeRepository employeeRepository;

    private static final String REFRESH_COOKIE_NAME = "refreshToken";

    // Refresh Token Operations

    @Transactional
    @Override
    public RefreshToken create(String username, String token, Instant expiry) {
        log.info("Create refresh token in auth service Impl");
        Employee emp = this.employeeRepository.getByEmail(username);
        this.refreshTokenRepository.deleteByEmployeeId(emp.getId());
        RefreshToken r = new RefreshToken();
        r.setEmployee(this.employeeRepository.getByEmail(username));
        r.setToken(token);
        r.setExpiryDate(expiry);
        log.info("Create refresh token in auth service Impl executed");
        return this.refreshTokenRepository.save(r);
    }

    @Transactional
    @Override
    public void deleteToken(String token) {
        log.info("delete token after logout auth service Impl");
        this.refreshTokenRepository.findByToken(token).ifPresent(refreshTokenRepository::delete);
    }

    @Override
    public Optional<RefreshToken> findByToken(String token) {
        log.info("search employee by token auth service Impl");
        return this.refreshTokenRepository.findByToken(token);
    }

    //Auth Operations

    @Override
    public ResponseEntity<?> login(String email, String password, HttpServletResponse response) {
        log.info("login in auth service impl triggered");
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(email, password)
        );

        UserDetails userDetails = this.customUserDetailService.loadUserByUsername(email);
        Employee emp = this.employeeRepository.findByEmail(email);

        // generate tokens
        String accessToken = this.jwtTokenHelper.generateAccessToken(userDetails, emp.getEmployeeRole().getName().name());
        String refreshToken = this.jwtTokenHelper.generateRefreshToken(userDetails);

        // store refresh token in DB
        Date refreshExp = this.jwtTokenHelper.getExpirationDateFromToken(refreshToken, true);
        create(email, refreshToken, refreshExp.toInstant());

        // send as HttpOnly cookie
        ResponseCookie cookie = ResponseCookie.from(REFRESH_COOKIE_NAME, refreshToken)
                .httpOnly(true)
                .secure(true)
                .path("/api/auth")
                .maxAge((refreshExp.getTime() - System.currentTimeMillis()) / 1000)
                .sameSite("Strict")
                .build();

        response.addHeader("Set-Cookie", cookie.toString());

        log.info("login in auth service impl executed");

        return ResponseEntity.ok(Map.of(
                "accessToken", accessToken,
                "user", emp
        ));
    }

    @Override
    public ResponseEntity<?> refreshToken(HttpServletRequest request, HttpServletResponse response) {
        log.info("Refresh token in auth service Impl triggered");
        var cookies = request.getCookies();
        if (cookies == null) {
            return ResponseEntity.status(AppConstants.SC_UNAUTHORIZED).body(Map.of("message", "Refresh token not found"));
        }

        String refreshToken = null;
        for (Cookie c : cookies) {
            if (REFRESH_COOKIE_NAME.equals(c.getName())) {
                refreshToken = c.getValue();
            }
        }
        if (refreshToken == null) {
            return ResponseEntity.status(AppConstants.SC_UNAUTHORIZED).body(Map.of("message", "Refresh token not found"));
        }

        if (!jwtTokenHelper.validateRefreshTokenSignature(refreshToken)) {
            return ResponseEntity.status(AppConstants.SC_UNAUTHORIZED).body(Map.of("message", "Invalid refresh token signature"));
        }

        var opt = findByToken(refreshToken);
        if (opt.isEmpty()) {
            return ResponseEntity.status(AppConstants.SC_UNAUTHORIZED).body(Map.of("message", "Refresh token not recognized"));
        }

        var dbToken = opt.get();
        if (dbToken.getExpiryDate().isBefore(Instant.now())) {
            deleteToken(refreshToken);
            return ResponseEntity.status(AppConstants.SC_UNAUTHORIZED).body(Map.of("message", "Refresh token expired"));
        }

        String username = this.jwtTokenHelper.getUsernameFromToken(refreshToken, true);
        UserDetails userDetails = this.customUserDetailService.loadUserByUsername(username);

        String newAccessToken = this.jwtTokenHelper.generateAccessToken(userDetails, dbToken.getEmployee().getEmployeeRole().getName().name());

        log.info("Refresh token in auth service Impl executed");

        return ResponseEntity.ok(Map.of("accessToken", newAccessToken));
    }

    @Override
    public ResponseEntity<?> logout(HttpServletRequest request, HttpServletResponse response) {
        log.info("Logout in auth service Impl triggered");
        var cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie c : cookies) {
                if (REFRESH_COOKIE_NAME.equals(c.getName())) {
                    String refreshToken = c.getValue();
                    deleteToken(refreshToken);
                }
            }
        }

        ResponseCookie cookie = ResponseCookie.from(REFRESH_COOKIE_NAME, "")
                .httpOnly(true)
                .secure(true)
                .path("/api/auth")
                .maxAge(0)
                .sameSite("Strict")
                .build();

        response.addHeader("Set-Cookie", cookie.toString());

        log.info("Logout in auth service Impl executed");

        return ResponseEntity.ok(Map.of("message", "Logged out"));
    }
}
