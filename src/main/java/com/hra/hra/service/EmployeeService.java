package com.hra.hra.service;

import com.hra.hra.dto.EmployeeDto;
import com.hra.hra.dto.Response;

import java.util.List;

public interface EmployeeService {

    Response register(EmployeeDto employeeDto);

    Response update(Long id,EmployeeDto employeeDto);

    Response deleteEmployee(Long id);

    Response getAllEmployee(Integer pageNumber, Integer pageSize, String sortBy, String sortDir);

    Response getEmployeeById(Long id);

    Response getEmployeeCount();

    // Update password when employee is already logged in
    Response updatePassword(String email,String newPassword, String confirmPassword);

    // Validate Employee is genuine or not
    Response validateEmployee(String email, String password);

    // Api to forgot password by sending verification link
    Response sendVerificationLink(String email);

    //change password after link verification
    Response changeNewPassword(String token, String newPassword, String confirmPassword);

    // Auth login to generate token to api access
    //Response authLogin(String email, String password);





}
