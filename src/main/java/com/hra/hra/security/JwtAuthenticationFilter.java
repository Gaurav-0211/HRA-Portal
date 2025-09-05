package com.hra.hra.security;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.SignatureException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;

import static org.aspectj.weaver.tools.cache.SimpleCacheFactory.path;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private JwtTokenHelper tokenHelper;

    private static final String[] PUBLIC_URLS = {
            "/api/auth/login",
            "/api/projects/**",
            "/api/products/**",
            "/api/employees/**"
    };

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        // 1. Skip filter for login endpoint
//        String requestPath = request.getRequestURI();
//        if (requestPath.contains("/api/auth/login")) {
//            filterChain.doFilter(request, response);
//            return;
//        }


        if (Arrays.stream(PUBLIC_URLS).anyMatch(path::startsWith)) {
            filterChain.doFilter(request, response);
            return;
        }

        String username = null;
        String token = null;

        // 2. Extract token from Authorization header
        String requestToken = request.getHeader("Authorization");
        if (requestToken != null && requestToken.startsWith("Bearer ")) {
            token = requestToken.substring(7);
        }

        // 3. If not found, check cookies
        if (token == null && request.getCookies() != null) {
            for (Cookie cookie : request.getCookies()) {
                if ("jwtToken".equals(cookie.getName())) {
                    token = cookie.getValue();
                    break;
                }
            }
        }

        // 4. Extract username from Access Token
        if (token != null) {
            try {
                username = this.tokenHelper.getUsernameFromToken(token, false); // false = access token
            } catch (IllegalArgumentException e) {
                request.setAttribute("jwt_exception", e);
                throw new AuthenticationServiceException("Unable to get JWT token", e);
            } catch (ExpiredJwtException e) {
                request.setAttribute("jwt_exception", e);
                throw new AuthenticationServiceException("JWT token has expired", e);
            } catch (MalformedJwtException e) {
                request.setAttribute("jwt_exception", e);
                throw new AuthenticationServiceException("Invalid JWT token format", e);
            } catch (SignatureException e) {
                request.setAttribute("jwt_exception", e);
                throw new AuthenticationServiceException("Invalid JWT token signature", e);
            } catch (UnsupportedJwtException e) {
                request.setAttribute("jwt_exception", e);
                throw new AuthenticationServiceException("Unsupported JWT token", e);
            }
        }

        // 5. Validate token & authenticate user
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);

            if (this.tokenHelper.validateAccessToken(token, userDetails)) {
                UsernamePasswordAuthenticationToken authenticationToken =
                        new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

                authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            } else {
                System.out.println("Invalid JWT Token");
            }
        }

        // 6. Continue filter chain
        filterChain.doFilter(request, response);
    }
}
