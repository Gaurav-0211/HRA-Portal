package com.hra.hra.service.impl;

import com.hra.hra.dto.EmployeeDto;
import com.hra.hra.entity.Employee;
import com.hra.hra.entity.Role;
import com.hra.hra.exception.EmployeeAlreadyExist;
import com.hra.hra.exception.NoEmployeeExist;
import com.hra.hra.exception.NoRoleExist;
import com.hra.hra.repository.EmployeeRepository;
import com.hra.hra.repository.RoleRepository;
import com.hra.hra.service.EmployeeService;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j

@Service
public class EmployeeServiceImpl implements EmployeeService {

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private ModelMapper mapper;

    @Autowired
    private RoleRepository roleRepository;

    // API to register a new Employee
    @Override
    public EmployeeDto register(EmployeeDto employeeDto) {
        log.info("Register api triggered in service Impl");
        Employee employee = this.mapper.map(employeeDto, Employee.class);

        // Fetch Role from DB
        Role role = this.roleRepository.findById(employeeDto.getRoleId())
                .orElseThrow(() -> new NoRoleExist("No Role Found"));

        // Set role in employee
        employee.setRole(role);

        // Set created and updated time
        employee.setCreatedAt(LocalDateTime.now());
        employee.setUpdatedAt(LocalDateTime.now());

        // Save employee
        Employee savedEmployee = employeeRepository.save(employee);
        log.info("User registered success service impl");
        return this.mapper.map(savedEmployee, EmployeeDto.class);
    }

    // API to update an existing user
    @Override
    public EmployeeDto update(EmployeeDto employeeDto) {
        log.info("Update employee in service Impl triggered");
        Employee employee = this.employeeRepository.getByEmail(employeeDto.getEmail());
        if(employee == null){
            throw new NoEmployeeExist("No employee found with the given email Id");
        }
        employee = this.mapper.map(employeeDto, Employee.class);
        employee.setUpdatedAt(LocalDateTime.now());
        this.employeeRepository.save(employee);
        log.info("Employee updated success service impl");
        return this.mapper.map(employee, EmployeeDto.class);
    }

    // API to delete an existing employee
    @Override
    public void deleteEmployee(Long id) {
        log.info("Delete employee triggered in service impl");
        Employee employee = this.employeeRepository.findById(id)
                .orElseThrow(()-> new NoEmployeeExist("No employee exist with given Id"));
        this.employeeRepository.delete(employee);
    }

    // API to get all employee
    @Override
    public List<EmployeeDto> getAllEmployee() {
        log.info("Get all employee triggered in service Impl");
        List<Employee> employees = this.employeeRepository.findAll();
        if(employees == null){
            throw new NoEmployeeExist("No employee has registered please register first");
        }
        return employees.stream().map((emp) -> this.mapper.map(emp, EmployeeDto.class)).collect(Collectors.toList());
    }

    // API to get employee by Id
    @Override
    public EmployeeDto getEmployeeById(Long id) {
        log.info("Get Employee by id triggered in service Impl");
        Employee employee = this.employeeRepository.findById(id)
                .orElseThrow(()-> new NoEmployeeExist("No employee exist with the given Id"));

        return this.mapper.map(employee, EmployeeDto.class);
    }
}
