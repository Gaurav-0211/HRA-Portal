package com.hra.hra.controller;

import com.hra.hra.dto.EmployeeDto;
import com.hra.hra.dto.Response;
import com.hra.hra.service.EmployeeService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/employees")
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;

    // POST Request to Register an Employee
    @PostMapping("/register")
    ResponseEntity<Response> registerEmployee( @RequestBody @Valid EmployeeDto employeeDto){

        Response response = this.employeeService.register(employeeDto);
        return  ResponseEntity.ok(response);
    }

    // PUT Request to update an existing employee
    @PutMapping("/update-employee")
    ResponseEntity<Response> updateEmployee(@RequestBody EmployeeDto employeeDto){
        Response response = this.employeeService.update(employeeDto);
        return ResponseEntity.ok(response);
    }

    // DELETE Request to remove an employee
    @DeleteMapping("/delete/{id}")
    ResponseEntity<Response> deleteEmployee(@PathVariable Long id){
        Response response= this.employeeService.deleteEmployee(id);

        return ResponseEntity.ok(response);
    }

    // GET Request to fetch all employees
    @GetMapping("/getAllEmployee")
    ResponseEntity<Response> getAllEmployee(){
        Response response = this.employeeService.getAllEmployee();

        return ResponseEntity.ok(response);
    }

    // GET Request to fetch a single Employee
    @GetMapping("/getEmployee/{id}")
    public ResponseEntity<Response> getEmpById(@PathVariable Long id){
        Response response = this.employeeService.getEmployeeById(id);

        return ResponseEntity.ok(response);
    }

}
