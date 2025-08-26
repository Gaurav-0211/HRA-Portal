package com.hra.hra.service;

import com.hra.hra.dto.EmployeeDto;
import com.hra.hra.dto.Response;

import java.util.List;

public interface EmployeeService {

    Response register(EmployeeDto employeeDto);

    Response update(EmployeeDto employeeDto);

    Response deleteEmployee(Long id);

    Response getAllEmployee();

    Response getEmployeeById(Long id);
}
