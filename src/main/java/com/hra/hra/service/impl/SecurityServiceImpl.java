//package com.hra.hra.service.impl;
//
//import com.hra.hra.config.AppConstants;
//import com.hra.hra.config.CustomUserDetailService;
//import com.hra.hra.dto.EmployeeDto;
//import com.hra.hra.dto.Response;
//import com.hra.hra.entity.Employee;
//import com.hra.hra.exception.NoDataExist;
//import com.hra.hra.repository.EmployeeRepository;
//import com.hra.hra.security.JwtTokenHelper;
//import com.hra.hra.service.SecurityService;
//import lombok.extern.slf4j.Slf4j;
//import org.modelmapper.ModelMapper;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.authentication.AuthenticationManager;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.stereotype.Service;
//import jakarta.servlet.http.Cookie;
//import jakarta.servlet.http.HttpServletResponse;
//@Slf4j
//@Service
//public class SecurityServiceImpl implements SecurityService {
//
//    @Autowired
//    private CustomUserDetailService customUserDetailService;
//
//    @Autowired
//    private ModelMapper mapper;
//
//    @Autowired
//    private JwtTokenHelper jwtTokenHelper;
//
//    @Autowired
//    private AuthenticationManager authenticationManager;
//
//    @Autowired
//    private EmployeeRepository employeeRepository;
//
//    @Autowired
//    private Response response;
//
//    // API to authenticated login and generate token for authentication and store in cookies to track
//    @Override
//    public Response authLogin(String email, String password, HttpServletResponse httpResponse) {
//        log.info("Auth login API in SecurityServiceImpl started");
//
//        // 1. Authenticate credentials
//        Authentication authentication = this.authenticationManager.authenticate(
//                new UsernamePasswordAuthenticationToken(email, password)
//        );
//
//        // 2. Load UserDetails
//        UserDetails userDetails = this.customUserDetailService.loadUserByUsername(email);
//
//        // 3. Get Employee from DB
//        Employee employee = this.employeeRepository.findByEmail(email);
//        if (employee == null) {
//            throw new NoDataExist("User does not exist with the given email");
//        }
//
//        // 4. Extract role from DB
//        String dbRole = employee.getEmployeeRole().getName().name();
//
//        // 5. Generate JWT with role
//        String token = this.jwtTokenHelper.generateToken(userDetails, dbRole);
//
//        // 6. Store token in HTTP-only cookie
//        Cookie cookie = new Cookie("jwtToken", token);
//        cookie.setHttpOnly(true);           // prevents JavaScript access (XSS protection)
//        cookie.setSecure(false);            // set true if you use HTTPS
//        cookie.setPath("/");                // cookie available for entire domain
//        cookie.setMaxAge(24 * 60 * 60);     // 1 day expiry
//        httpResponse.addCookie(cookie);
//
//        // 7. Build response
//        response.setStatus("SUCCESS");
//        response.setMessage("Login successful");
//        response.setData(token); // optionally return employee info instead of token
//        response.setStatusCode(AppConstants.OK);
//        response.setResponse_message("Process execution success");
//
//        log.info("Auth login API executed and token generated token : {}", token);
//        return response;
//    }
//
//}
