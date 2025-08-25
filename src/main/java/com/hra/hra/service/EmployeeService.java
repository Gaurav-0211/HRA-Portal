package com.hra.hra.service;

import com.hra.hra.dto.EmployeeDto;

import java.util.List;

public interface EmployeeService {

    EmployeeDto register(EmployeeDto employeeDto);

    EmployeeDto update(EmployeeDto employeeDto);

    void deleteEmployee(Long id);

    List<EmployeeDto> getAllEmployee();

    EmployeeDto getEmployeeById(Long id);
}
