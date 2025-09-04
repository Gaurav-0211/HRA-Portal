package com.hra.hra.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.security.core.userdetails.UserDetails;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.Map;
import java.util.function.Function;

@Component
public class JwtTokenHelper {

    @Value("${app.jwt.access-secret}")
    private String accessSecret;

    @Value("${app.jwt.refresh-secret}")
    private String refreshSecret;

    @Value("${app.jwt.access-expiration-ms}")
    private long accessExpirationMs;

    @Value("${app.jwt.refresh-expiration-ms}")
    private long refreshExpirationMs;

    private SecretKey accessKey() {
        return Keys.hmacShaKeyFor(accessSecret.getBytes(StandardCharsets.UTF_8));
    }

    private SecretKey refreshKey() {
        return Keys.hmacShaKeyFor(refreshSecret.getBytes(StandardCharsets.UTF_8));
    }

    // Generic claim reader
    public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver, boolean isRefresh) {
        final Claims claims = getAllClaimsFromToken(token, isRefresh);
        return claimsResolver.apply(claims);
    }

    private Claims getAllClaimsFromToken(String token, boolean isRefresh) {
        return Jwts.parserBuilder()
                .setSigningKey(isRefresh ? refreshKey() : accessKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public String getUsernameFromToken(String token, boolean isRefresh) {
        return getClaimFromToken(token, Claims::getSubject, isRefresh);
    }

    public Date getExpirationDateFromToken(String token, boolean isRefresh) {
        return getClaimFromToken(token, Claims::getExpiration, isRefresh);
    }

    public boolean isTokenExpired(String token, boolean isRefresh) {
        Date exp = getExpirationDateFromToken(token, isRefresh);
        return exp.before(new Date());
    }

    // generate access token
    public String generateAccessToken(UserDetails userDetails, String role) {
        Map<String, Object> claims = Map.of("role", role);
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + accessExpirationMs))
                .signWith(accessKey(), SignatureAlgorithm.HS512)
                .compact();
    }

    // generate refresh token (long lived)
    public String generateRefreshToken(UserDetails userDetails) {
        return Jwts.builder()
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + refreshExpirationMs))
                .signWith(refreshKey(), SignatureAlgorithm.HS512)
                .compact();
    }

    public boolean validateAccessToken(String token, UserDetails userDetails) {
        final String username = getUsernameFromToken(token, false);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token, false));
    }

    public boolean validateRefreshTokenSignature(String token) {
        try {
            // this will throw if signature invalid or token invalid
            getAllClaimsFromToken(token, true);
            return true;
        } catch (JwtException ex) {
            return false;
        }
    }
}
