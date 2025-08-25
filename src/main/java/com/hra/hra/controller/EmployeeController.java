package com.hra.hra.controller;

import com.hra.hra.dto.EmployeeDto;
import com.hra.hra.dto.Response;
import com.hra.hra.service.EmployeeService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/employees")
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;

    // POST Request to Register an Employee
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

    // PUT Request to update an existing employee
    @PutMapping("/update-employee")
    ResponseEntity<Response> updateEmployee(@RequestBody EmployeeDto employeeDto){
        EmployeeDto dto = this.employeeService.update(employeeDto);
        Response response = Response.buildResponse(
                "SUCCESS",
                "Employee updated success",
                dto,
                200,
                "Execution completed"
        );
        return ResponseEntity.ok(response);
    }

    // DELETE Request to remove an employee
    @DeleteMapping("/delete/{id}")
    ResponseEntity<Response> deleteEmployee(@RequestParam Long id){
        this.employeeService.deleteEmployee(id);
        Response response = Response.buildResponse(
                "SUCCESS",
                "Employee Deleted Success",
                null,
                200,
                "Execution Completed"
        );
        return ResponseEntity.ok(response);
    }

    // GET Request to fetch all employees
    @GetMapping("/getAllEmployee")
    ResponseEntity<Response> getAllEmployee(){
        List<EmployeeDto> employees = this.employeeService.getAllEmployee();
        Response response = Response.buildResponse(
                "SUCCESS",
                "All Employee Fetched",
                employees,
                200,
                "Execution completed"
        );
        return ResponseEntity.ok(response);
    }

    // GET Request to fetch a single Employee
    @GetMapping("/getEmployee/{id}")
    public ResponseEntity<Response> getEmpById(@RequestParam Long id){
        EmployeeDto dto = this.employeeService.getEmployeeById(id);
        Response response = Response.buildResponse(
                "SUCCESS",
                "Employee fetched ",
                dto,
                200,
                "Execution completed"
        );
        return ResponseEntity.ok(response);
    }

}
