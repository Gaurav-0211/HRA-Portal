package com.hra.hra.controller;

import com.hra.hra.dto.EmployeeDto;
import com.hra.hra.dto.Response;
import com.hra.hra.service.EmployeeService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/employees")
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;

    @PostMapping("/register")
    ResponseEntity<Response> registerEmployee( @RequestBody @Valid EmployeeDto employeeDto){
        EmployeeDto employeeDto1 = this.employeeService.register(employeeDto);
        Response response = Response.buildResponse(
                "SUCCESS",
                "Employee Registered Success",
                employeeDto1,
                200,
                "Execution done"
        );
        return  ResponseEntity.ok(response);
    }
}
