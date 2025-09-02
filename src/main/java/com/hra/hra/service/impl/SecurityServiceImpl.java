package com.hra.hra.service.impl;

import com.hra.hra.config.AppConstants;
import com.hra.hra.config.CustomUserDetailService;
import com.hra.hra.dto.Response;
import com.hra.hra.entity.Employee;
import com.hra.hra.exception.NoDataExist;
import com.hra.hra.repository.EmployeeRepository;
import com.hra.hra.security.JwtTokenHelper;
import com.hra.hra.service.SecurityService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
@Slf4j
@Service
public class SecurityServiceImpl implements SecurityService {

    @Autowired
    private CustomUserDetailService customUserDetailService;

    @Autowired
    private JwtTokenHelper jwtTokenHelper;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private Response response;

    // API to generate token for authentication
    @Override
    public Response authLogin(String email, String password, String role) {
        log.info("Auth login api in security service Impl");
        // 1. Authenticate credentials
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(email, password)
        );

        // 2. Load UserDetails
        UserDetails userDetails = this.customUserDetailService.loadUserByUsername(email);

        // 3. Get agent from DB
        Employee employee = this.employeeRepository.findByEmail(email);
        if (employee == null) {
            throw new NoDataExist("User not exist with the given mail Id");
        }

        // 4. Compare role
        String dbRole = employee.getEmployeeRole().getName().name(); // assuming enum stored in DB

        if (!dbRole.equalsIgnoreCase(role)) {
            throw new NoDataExist("Access denied: Role mismatch. You are not authorized as " + role);
        }

        // 5. Generate JWT with role
        String token = this.jwtTokenHelper.generateToken(userDetails, dbRole);

        response.setStatus("SUCCESS");
        response.setMessage("Token generated successfully");
        response.setData(token);
        response.setStatusCode(AppConstants.OK);
        response.setResponse_message("Process execution success");
        log.info("Auth login api in security service Impl executed");

        return response;
    }
}
