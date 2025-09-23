package com.hra.hra.controller;

import com.hra.hra.config.AppConstants;
import com.hra.hra.dto.*;
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
    ResponseEntity<Response> updateEmployee(@PathVariable Long id, @Valid @RequestBody EmployeeDto employeeDto){
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
    ResponseEntity<Response> getAllEmployee(
            @RequestParam(value = "pageNumber", defaultValue = AppConstants.PAGE_NUMBER, required = false) Integer pageNumber,
            @RequestParam(value = "pageSize", defaultValue = AppConstants.PAGE_SIZE, required = false) Integer pageSize,
            @RequestParam(value = "sortBy", defaultValue = AppConstants.SORT_BY, required = false) String sortBy,
            @RequestParam(value = "sortDir", defaultValue = AppConstants.SORT_DIR, required = false) String sortDir
    ){
        log.info("Get all employee in controller");
        Response response = this.employeeService.getAllEmployee(pageNumber, pageSize, sortBy, sortDir);
        log.info("Get all employee in controller executed");
        return ResponseEntity.ok(response);
    }

    // GET Request to fetch a single Employee
    @GetMapping("/getEmployee/{id}")
    public ResponseEntity<Response> getEmpById(@PathVariable Long id){
        log.info("Get single employee in controller");

        // Validate ID
        if (id == null || id <= 0) {
            Response errorResponse = new Response();
            errorResponse.setStatus("ERROR");
            errorResponse.setMessage("Invalid employee ID");
            errorResponse.setData(null);
            errorResponse.setStatusCode(400);
            errorResponse.setResponse_message("Process execution failed");
            return ResponseEntity.badRequest().body(errorResponse);
        }

        // Fetch leave
        Response response = this.employeeService.getEmployeeById(id);
        log.info("Get single leave in controller executed");

        return ResponseEntity.ok(response);
    }

    // GET request to count total employee in the organization
    @GetMapping("/getEmployeeCount")
    public ResponseEntity<Response> getEmpCount(){
        log.info("Get employee count in controller");
        Response response = this.employeeService.getEmployeeCount();
        log.info("Get employee count in controller executed");
        return ResponseEntity.ok(response);
    }


    // Post Request to log in Request without authentication
    @PostMapping("/login")
    public ResponseEntity<Response> login(@RequestBody LoginRequest loginRequest) {
        log.info("Login request in employee controller");

        Response response = this.employeeService.validateEmployee(loginRequest.getEmail(), loginRequest.getPassword());
        log.info("Login request employee controller executed");
        return ResponseEntity.ok(response);
    }

    // POST API to send verification link on email
    @PostMapping("/send-forgot-link")
    public ResponseEntity<Response> sendLinkEmail(@RequestParam String email) {
        log.info("Send verification link on email employee controller");
        Response response = this.employeeService.sendVerificationLink(email);
        log.info("send verification link on email employee controller executed");
        return ResponseEntity.ok(response);
    }

    // POST request to forgot password with the link
    @PostMapping("/forgot-password")
    public ResponseEntity<Response> forgotPasswordWithLink(@RequestParam String token,
                                                           @RequestBody ResetPasswordRequest request) {
        log.info("verify token and update password in employee controller");
        Response response = this.employeeService.changeNewPassword(token, request.getNewPassword(), request.getConfirmPassword());
        log.info("verify token and update password in employee controller executed");

        return ResponseEntity.ok(response);
    }


    // PATCH request to Change Password when employee is logged in and changing it from internal application
    @PatchMapping("/change-password")
    public ResponseEntity<Response> changePassword(@RequestBody @Valid PasswordChangeDto passwordChangeDto) {
        log.info("Change password in controller");

        Response response =  this.employeeService.updatePassword(passwordChangeDto.getEmail(), passwordChangeDto.getOldPassword(), passwordChangeDto.getNewPassword());
        log.info("Change password internally controller executed");
        return ResponseEntity.ok(response);
    }

}
