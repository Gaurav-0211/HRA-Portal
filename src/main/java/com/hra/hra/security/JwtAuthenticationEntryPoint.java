package com.hra.hra.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hra.hra.config.AppConstants;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.SignatureException;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint, Serializable {

    @Override
    public void commence(HttpServletRequest request,
                         HttpServletResponse response,
                         AuthenticationException authException) throws IOException, ServletException {

        Throwable jwtException = (Throwable) request.getAttribute("jwt_exception");

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        Map<String, Object> responseBody = new HashMap<>();
        responseBody.put("timestamp", LocalDateTime.now().toString());
        responseBody.put("path", request.getRequestURI());

        if (jwtException instanceof ExpiredJwtException)
        {
            response.setStatus(AppConstants.SC_UNAUTHORIZED);
            responseBody.put("status", AppConstants.SC_UNAUTHORIZED);
            responseBody.put("error", "Token Expired");
            responseBody.put("message", "Token Expired, please re-login");
        }
        else if (jwtException instanceof MalformedJwtException)
        {
            response.setStatus(AppConstants.SC_UNAUTHORIZED);
            responseBody.put("status", AppConstants.SC_UNAUTHORIZED);
            responseBody.put("error", "Invalid Token");
            responseBody.put("message", "Invalid Token Id, please re-check it");
        }
        else if (jwtException instanceof SignatureException)
        {
            response.setStatus(AppConstants.SC_UNAUTHORIZED);
            responseBody.put("status", AppConstants.SC_UNAUTHORIZED);
            responseBody.put("error", "Invalid Signature");
            responseBody.put("message", "Invalid Jwt Token Signature");
        }
        else if (jwtException instanceof UnsupportedJwtException)
        {
            response.setStatus(AppConstants.UNSUPPORTED);
            responseBody.put("status", AppConstants.UNSUPPORTED);
            responseBody.put("error", "Unsupported Token");
            responseBody.put("message", "Unsupported Request");
        }
        else
        {
            // Fallback for other authentication failures
            response.setStatus(AppConstants.SC_UNAUTHORIZED);
            responseBody.put("status", AppConstants.SC_UNAUTHORIZED);
            responseBody.put("error", "Unauthorized");
            responseBody.put("message", "You are not authorised to make changes in db");
        }
        new ObjectMapper().writeValue(response.getWriter(), responseBody);
    }
}
