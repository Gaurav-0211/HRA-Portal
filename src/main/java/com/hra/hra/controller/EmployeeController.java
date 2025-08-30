package com.hra.hra.controller;

import com.hra.hra.dto.EmployeeDto;
import com.hra.hra.dto.Response;
import com.hra.hra.service.EmployeeService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
@Slf4j
@RestController
@RequestMapping("/api/employees")
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;

    // POST Request to Register an Employee
    @PostMapping("/register")
    public ResponseEntity<Response> registerEmployee( @RequestBody @Valid EmployeeDto employeeDto){
        log.info("Register employee in controller");

        Response response = this.employeeService.register(employeeDto);
        return  ResponseEntity.ok(response);
    }

    // PUT Request to update an existing employee
    @PutMapping("/update-employee/{id}")
    ResponseEntity<Response> updateEmployee(@PathVariable Long id, @RequestBody EmployeeDto employeeDto){
        log.info("Update employee in controller");
        Response response = this.employeeService.update(id,employeeDto);
        log.info("Update employee in controller executed");
        return ResponseEntity.ok(response);
    }

    // DELETE Request to remove an employee
    @DeleteMapping("/delete/{id}")
    ResponseEntity<Response> deleteEmployee(@PathVariable Long id){
        log.info("Delete employee in controller ");
        Response response= this.employeeService.deleteEmployee(id);
        log.info("Delete employee in controller executed");
        return ResponseEntity.ok(response);
    }

    // GET Request to fetch all employees
    @GetMapping("/getAllEmployee")
    ResponseEntity<Response> getAllEmployee(){
        log.info("Get all employee in controller");
        Response response = this.employeeService.getAllEmployee();
        log.info("Get all employee in controller executed");
        return ResponseEntity.ok(response);
    }

    // GET Request to fetch a single Employee
    @GetMapping("/getEmployee/{id}")
    public ResponseEntity<Response> getEmpById(@PathVariable Long id){
        log.info("Get employee by Id in controller");
        Response response = this.employeeService.getEmployeeById(id);
        log.info("Get employee by Id in controller executed");
        return ResponseEntity.ok(response);
    }

}
