package com.hra.hra.controller;

import com.hra.hra.config.AppConstants;
import com.hra.hra.config.CustomUserDetailService;
import com.hra.hra.dto.*;
import com.hra.hra.entity.Employee;
import com.hra.hra.exception.NoDataExist;
import com.hra.hra.repository.EmployeeRepository;
import com.hra.hra.security.JwtTokenHelper;
import com.hra.hra.service.EmployeeService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
@Slf4j
@RestController
@RequestMapping("/api/employees")
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;

    @Autowired
    private CustomUserDetailService customUserDetailService;

    @Autowired
    private JwtTokenHelper jwtTokenHelper;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private EmployeeRepository employeeRepository;

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
        log.info("Get employee by Id in controller");
        Response response = this.employeeService.getEmployeeById(id);
        log.info("Get employee by Id in controller executed");
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


    //Post request to Authenticated loing for token generation
    @PostMapping("/auth-login")
    public ResponseEntity<Response> authLogin(@RequestBody LoginDto request) {
        // 1. Authenticate credentials
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );

        // 2. Load UserDetails
        UserDetails userDetails = this.customUserDetailService.loadUserByUsername(request.getEmail());

        // 3. Get agent from DB
        Employee employee = this.employeeRepository.findByEmail(request.getEmail());
        if (employee == null) {
            throw new NoDataExist("User not exist with the given mail Id");
        }

        // 4. Compare role
        String dbRole = employee.getEmployeeRole().getName().name(); // assuming enum stored in DB
        String requestRole = request.getRole().name();

        if (!dbRole.equalsIgnoreCase(requestRole)) {
            throw new NoDataExist("Access denied: Role mismatch. You are not authorized as " + requestRole);
        }

        // 5. Generate JWT with role
        String token = jwtTokenHelper.generateToken(userDetails, dbRole);

        Response response = new Response();
        response.setStatus("SUCCESS");
        response.setMessage("Token generated successfully");
        response.setData(token);
        response.setStatusCode(AppConstants.OK);
        response.setResponse_message("Process execution success");

        return ResponseEntity.ok(response);
    }

}
