package com.hra.hra.security;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
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
import io.jsonwebtoken.security.SignatureException;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private JwtTokenHelper tokenHelper;


    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        // 1. Bypass filter for login endpoint
        String requestPath = request.getRequestURI();
        if (requestPath.contains("/api/auth/login")) {
            filterChain.doFilter(request, response);
            return;
        }

        // 2. Extract JWT token
        final String requestToken = request.getHeader("Authorization");
        String username = null;
        String token = null;

        if (requestToken != null && requestToken.startsWith("Bearer ")) {
            token = requestToken.substring(7);
            try {
                username = this.tokenHelper.getUserNameFromToken(token);
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
        } else {
            System.out.println("JWT Token does not begin with Bearer");
        }


        // 3. Validate token and set authentication
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);

            if (this.tokenHelper.validateToken(token, userDetails)) {
                UsernamePasswordAuthenticationToken authenticationToken =
                        new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

                authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            } else {
                System.out.println("Invalid JWT Token");
            }
        } else if (username == null) {
            System.out.println("Username is null, skipping authentication");
        }

        // 4. Continue filter chain
        filterChain.doFilter(request, response);
    }
}