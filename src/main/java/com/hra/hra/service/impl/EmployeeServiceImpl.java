package com.hra.hra.service.impl;

import com.hra.hra.config.AppConstants;
import com.hra.hra.dto.EmployeeDto;
import com.hra.hra.dto.Response;
import com.hra.hra.entity.Department;
import com.hra.hra.entity.Employee;
import com.hra.hra.entity.Role;
import com.hra.hra.exception.NoDepartmentExist;
import com.hra.hra.exception.NoEmployeeExist;
import com.hra.hra.exception.NoRoleExist;
import com.hra.hra.repository.DepartmentRepository;
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
    private DepartmentRepository departmentRepository;

    @Autowired
    private ModelMapper mapper;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private Response response;

    // API to register a new Employee
    @Override
    public Response register(EmployeeDto employeeDto) {
        log.info("Register api triggered in service Impl");
        Employee employee = this.mapper.map(employeeDto, Employee.class);

        // Fetch Role from DB
        Role role = this.roleRepository.findById(employeeDto.getRoleId())
                .orElseThrow(() -> new NoRoleExist("No Role Found"));
        // Set role in employee
        employee.setRole(role);

        Department department = this.departmentRepository.findById(employeeDto.getDepartmentId())
                .orElseThrow(() -> new NoDepartmentExist("No Department Found"));
        employee.setDepartment(department);

        // Set created and updated time
        employee.setCreatedAt(LocalDateTime.now());
        employee.setUpdatedAt(LocalDateTime.now());

        // Save employee
        Employee savedEmployee = employeeRepository.save(employee);
        response.setStatus("SUCCESS");
        response.setStatusCode(AppConstants.CREATED);
        response.setMessage("Employee Registered Success");
        response.setData(this.mapper.map(savedEmployee, EmployeeDto.class));
        response.setResponse_message("Process Completed");
        log.info("User registered success service impl");
        return response;
    }

    // API to update an existing user
    @Override
    public Response update(EmployeeDto employeeDto) {
        log.info("Update employee in service Impl triggered");
        Employee employee = this.employeeRepository.getByEmail(employeeDto.getEmail());
        if(employee == null){
            throw new NoEmployeeExist("No employee found with the given email Id");
        }
        employee = this.mapper.map(employeeDto, Employee.class);
        employee.setUpdatedAt(LocalDateTime.now());
        this.employeeRepository.save(employee);
        log.info("Employee updated success service impl");

        // Sending a proper response from service Impl
        response.setStatus("SUCCESS");
        response.setStatusCode(AppConstants.OK);
        response.setMessage("Employee Updated Success");
        response.setData(this.mapper.map(employee, EmployeeDto.class));
        response.setResponse_message("Execution process Completed");
        return response;
    }

    // API to delete an existing employee
    @Override
    public Response deleteEmployee(Long id) {
        log.info("Delete employee triggered in service impl");
        Employee employee = this.employeeRepository.findById(id)
                .orElseThrow(()-> new NoEmployeeExist("No employee exist with given Id"+id));
        response.setStatus("SUCCESS");
        response.setStatusCode(AppConstants.OK);
        response.setMessage("Employee Deletion Success");
        response.setData(this.mapper.map(employee, EmployeeDto.class));
        response.setResponse_message("Execution process completed");
        return response;
    }

    // API to get all employee
    @Override
    public Response getAllEmployee() {
        log.info("Get all employee triggered in service Impl");
        List<Employee> employees = this.employeeRepository.findAll();
        if(employees == null){
            throw new NoEmployeeExist("No employee has registered please register first");
        }
        response.setStatus("SUCCESS");
        response.setStatusCode(AppConstants.OK);
        response.setMessage("All Employee Fetched Success");
        response.setData(employees.stream().map((emp) -> this.mapper.map(emp, EmployeeDto.class)).collect(Collectors.toList()));
        response.setResponse_message("Process execution completed");
        return response;
    }

    // API to get employee by Id
    @Override
    public Response getEmployeeById(Long id) {
        log.info("Get Employee by id triggered in service Impl");
        Employee employee = this.employeeRepository.findById(id)
                .orElseThrow(()-> new NoEmployeeExist("No employee exist with the given Id"));

        response.setStatus("SUCCESS");
        response.setStatusCode(AppConstants.OK);
        response.setMessage("Employee Fetched Success");
        response.setData(this.mapper.map(employee, EmployeeDto.class));
        response.setResponse_message("Process execution completed");
        return response;
    }
}
