package com.hra.hra.service.impl;

import com.hra.hra.config.AppConstants;
import com.hra.hra.dto.EmployeeDto;
import com.hra.hra.dto.PageResponse;
import com.hra.hra.dto.Response;
import com.hra.hra.entity.Department;
import com.hra.hra.entity.Employee;
import com.hra.hra.entity.EmployeeRole;
import com.hra.hra.entity.Role;
import com.hra.hra.exception.NoDataExist;
import com.hra.hra.exception.NoDepartmentExist;
import com.hra.hra.exception.NoRoleExist;
import com.hra.hra.repository.DepartmentRepository;
import com.hra.hra.repository.EmployeeRepository;
import com.hra.hra.repository.EmployeeRoleRepository;
import com.hra.hra.repository.RoleRepository;
import com.hra.hra.service.EmployeeService;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
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

    @Autowired
    private PasswordEncoder passwordEncoder;

    private boolean flag = false;

    @Autowired
    private JavaMailSender javaMailSender;

    @Autowired
    private EmployeeRoleRepository employeeRoleRepository;

    @Autowired
    private LeaveServiceImplementation leaveServiceImplementation;


    // API to register a new Employee
    @Override
    public Response register(EmployeeDto employeeDto) {
        log.info("Register api triggered in service Impl");
        Employee emp  = this.employeeRepository.findByEmail(employeeDto.getEmail());
        if(emp != null){
            response.setStatus("FAILED");
            response.setMessage("Email Id already registered");
            response.setData(null);
            response.setStatusCode(202);
            response.setResponse_message("Process execution success");

            return response;
        }
        Employee employee = this.mapper.map(employeeDto, Employee.class);
        employee.setPassword(passwordEncoder.encode(employeeDto.getPassword()));

        // Fetch Role from DB
        Role role = this.roleRepository.findById(employeeDto.getRoleId())
                .orElseThrow(() -> new NoRoleExist("No Role Found"));
        // Set role in employee
        employee.setRole(role);

        Department department = this.departmentRepository.findById(employeeDto.getDepartmentId())
                .orElseThrow(() -> new NoDepartmentExist("No Department Found"));
        employee.setDepartment(department);

        EmployeeRole employeeRole = this.employeeRoleRepository.findById(employeeDto.getEmployeeRoleId())
                .orElseThrow(() -> new NoDataExist("No Role Found "));
        employee.setEmployeeRole(employeeRole);

        // Set created and updated time
        employee.setCreatedAt(LocalDateTime.now());
        employee.setUpdatedAt(LocalDateTime.now());
        employee.setJoinDate(LocalDate.now());
        employee.setActive(true);


        // Save employee
        Employee savedEmployee = this.employeeRepository.save(employee);

        // Initialize balances for the joining month
        this.leaveServiceImplementation.initializeBalancesForJoin(savedEmployee);

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
    public Response update(Long id, EmployeeDto dto) {
        log.info("Update employee in service Impl triggered");
        Employee employee = this.employeeRepository.findById(id)
                .orElseThrow(()->new NoDataExist("No Employee Exist with given Id"));

        // update only the fields you want
        employee.setName(dto.getName());
        employee.setEmail(dto.getEmail());
        employee.setPassword(passwordEncoder.encode(dto.getPassword()));
        employee.setContactNumber(dto.getContactNumber());
        employee.setAddress(dto.getAddress());

        if (dto.getRoleId() != null) {
            Role role = this.roleRepository.findById(dto.getRoleId())
                    .orElseThrow(() -> new NoDataExist("Role not found"));
            employee.setRole(role);
        }

        if (dto.getEmployeeRoleId() != null) {
            EmployeeRole empRole = this.employeeRoleRepository.findById(dto.getEmployeeRoleId())
                    .orElseThrow(() -> new NoDataExist("EmployeeRole not found"));
            employee.setEmployeeRole(empRole);
        }

        if (dto.getDepartmentId() != null) {
            Department dept = this.departmentRepository.findById(dto.getDepartmentId())
                    .orElseThrow(() -> new NoDataExist("Department not found"));
            employee.setDepartment(dept);
        }

        Employee saved = this.employeeRepository.save(employee);

        // Sending a proper response from service Impl
        response.setStatus("SUCCESS");
        response.setStatusCode(AppConstants.OK);
        response.setMessage("Employee Updated Success");
        response.setData(this.mapper.map(saved, EmployeeDto.class));
        response.setResponse_message("Execution process Completed");
        log.info("Update employee in service Impl executed");
        return response;
    }

    // API to delete an existing employee
    @Override
    public Response deleteEmployee(Long id) {
        log.info("Delete employee triggered in service impl");
        Employee employee = this.employeeRepository.findById(id)
                .orElseThrow(()-> new NoDataExist("No employee exist with given Id"+id));
        response.setStatus("SUCCESS");
        response.setStatusCode(AppConstants.OK);
        response.setMessage("Employee Deletion Success");
        response.setData(this.mapper.map(employee, EmployeeDto.class));
        response.setResponse_message("Execution process completed");
        log.info("Delete employee in service impl executed");

        return response;
    }

    // API to get all employee
    @Override
    public Response getAllEmployee(Integer pageNumber, Integer pageSize, String sortBy, String sortDir)
    {
        log.info("Get all employee triggered in service Impl");
        Sort sort = sortDir != null && sortDir.equalsIgnoreCase("asc")
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);
        Page<Employee> employeePage = this.employeeRepository.findAll(pageable);

        List<EmployeeDto> employeeDtoList = employeePage
                .getContent()
                .stream()
                .map(emp -> mapper.map(emp, EmployeeDto.class))
                .collect(Collectors.toList());

        log.info("All Employee converted into pages in employee service impl");
        PageResponse<EmployeeDto> obj = PageResponse.<EmployeeDto>builder()
                .content(employeeDtoList)
                .pageNumber(employeePage.getNumber())
                .pageSize(employeePage.getSize())
                .totalElements(employeePage.getTotalElements())
                .totalPage(employeePage.getTotalPages())
                .lastPage(employeePage.isLast())
                .build();

        response.setStatus("SUCCESS");
        response.setMessage("All Department fetched successfully");
        response.setData(obj);
        response.setStatusCode(AppConstants.OK);
        response.setResponse_message("Execution process completed");
        log.info("Get all employee in service Impl executed");

        return response;
    }

    // API to get employee by Id
    @Override
    public Response getEmployeeById(Long id) {
        log.info("Get Employee by id triggered in service Impl");
        Employee employee = this.employeeRepository.findById(id)
                .orElseThrow(()-> new NoDataExist("No employee exist with the given Id"));

        response.setStatus("SUCCESS");
        response.setStatusCode(AppConstants.OK);
        response.setMessage("Employee Fetched Success");
        response.setData(this.mapper.map(employee, EmployeeDto.class));
        response.setResponse_message("Process execution completed");
        log.info("Get Employee by id in service Impl executed");

        return response;
    }

    @Override
    public Response getEmployeeCount() {
        log.info("Get Employee count service Impl");
        Long result = this.employeeRepository.count();

        response.setStatus("SUCCESS");
        response.setStatusCode(AppConstants.OK);
        response.setMessage("Employee Count Success");
        response.setData(result);
        response.setResponse_message("Process execution completed");
        log.info("Get Employee count service Impl executed");

        return response;
    }

    // API to change password when employee is already logged in
    @Override
    public Response updatePassword(String email, String newPassword, String confirmPassword) {
        Employee employee = this.employeeRepository.findByEmail(email);
        if (employee == null) {
            throw new NoDataExist("No user exist with email : " + email);
        }

        // Check if new password is not equal to new Confirm password
        if(!newPassword.equals(confirmPassword)){
            throw new NoDataExist("Password and Confirm Password does not match");
        }

        // Matching old password with new password
        if (passwordEncoder.matches(newPassword, employee.getPassword())) {
            throw new NoDataExist("New password cannot be same as old password");
        }

        employee.setPassword(passwordEncoder.encode(newPassword));
        log.info("Password fetched and updated employee service impl");
        this.employeeRepository.save(employee);

        response.setStatus("SUCCESS");
        response.setMessage("Employee password updated success");
        response.setData(null);
        response.setStatusCode(AppConstants.OK);
        response.setResponse_message("Process execution success");
        return response;
    }

    @Override
    public Response validateEmployee(String email, String password) {
        log.info("Validate(Login) employee in Impl- Service");
        Employee employee = this.employeeRepository.findByEmail(email);
        if (employee == null) {
            flag = false;
        }

        // Check if account lock expired
        if (employee.getAccountLockedUntil() != null) {
            if (LocalDateTime.now().isAfter(employee.getAccountLockedUntil())) {
                // Lock expired → reset fields
                employee.setAccountLockedUntil(null);
                employee.setFailedLoginAttempts(0);
                employeeRepository.save(employee);
                log.info("Lock expired → reset failed attempts and unlock account");
            } else {
                // If Still locked
                long secondsRemaining = java.time.Duration
                        .between(LocalDateTime.now(), employee.getAccountLockedUntil())
                        .getSeconds();
                throw new NoDataExist("Account is locked. Please wait "
                        + secondsRemaining + " seconds before trying again.");
            }
        }

        log.info("User is not locked, validating credentials");

        if (passwordEncoder.matches(password, employee.getPassword())) {
            // Reset failed attempts after successful login
            employee.setFailedLoginAttempts(0);
            employee.setAccountLockedUntil(null);
            employeeRepository.save(employee);
            flag =  true;
        } else {
            // Increment failed attempts
            int newAttempts = employee.getFailedLoginAttempts() + 1;
            employee.setFailedLoginAttempts(newAttempts);

            if (newAttempts > 3) {
                employee.setAccountLockedUntil(LocalDateTime.now().plusMinutes(5));
                employeeRepository.save(employee);
                log.info("User locked for " + 5 + " minutes due to invalid credentials");
                throw new NoDataExist("Too many failed attempts. Account locked for "
                        + 5 + " minutes.");
            }

            employeeRepository.save(employee);
            flag = false;
        }
        if(flag ){
            response.setStatus("SUCCESS");
            response.setMessage("Employee logged in success");
            response.setData(null);
            response.setStatusCode(AppConstants.OK);

        }else{
            response.setStatus("FAILED");
            response.setMessage("Invalid login Credentials");
            response.setData(null);
            response.setStatusCode(AppConstants.SC_UNAUTHORIZED);

        }
        response.setResponse_message("Execution process completed");
        return response;
    }

    @Override
    public Response sendVerificationLink(String email) {
        Employee employee = this.employeeRepository.findByEmail(email);
        if (employee == null) {
            throw new NoDataExist("No user exist with email: " + email);
        }

        // Acknowledge of resending link again within 5 minutes
        if (employee.getLastResetLinkSentAt() != null &&
                LocalDateTime.now().isBefore(employee.getLastResetLinkSentAt().plusMinutes(5))) {
            throw new NoDataExist("You can request another reset link after 5 minutes.");
        }

        // Generate token and expiry of the link
        String token = UUID.randomUUID().toString();
        employee.setResetToken(token);
        employee.setResetTokenExpiry(LocalDateTime.now().plusMinutes(5));
        employee.setLastResetLinkSentAt(LocalDateTime.now());
        employeeRepository.save(employee);

        // Reset link point to forgot react page where user will add new password and confirm password
        String resetLink = "http://localhost:5173/forgot-password?token=" + token;

        // Send email
        sendEmailLink(email, resetLink);

        response.setStatus("SUCCESS");
        response.setStatusCode(AppConstants.OK);
        response.setMessage("Reset password Email sent Success");
        response.setData(null);
        response.setResponse_message("Process execution completed");

        return response;
    }

    // Validate email annotation for sending email
    @Value("$(spring.mail.username)")
    private String fromEmailId;

    private void sendEmailLink(String email, String resetLink){

        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setFrom(fromEmailId);
        mailMessage.setTo(email);
        mailMessage.setText("Please Click on the link to reset your password : "+ resetLink );
        mailMessage.setSubject("Reset Password Link" );

        log.info("Verification Link Sent Successful");
        javaMailSender.send(mailMessage);
    }


    @Override
    public Response changeNewPassword(String token, String newPassword, String confirmPassword) {
        Employee employee = this.employeeRepository.findByResetToken(token);
        if (employee == null) {
            throw new NoDataExist("Invalid reset link.");
        }

        if (employee.getResetTokenExpiry() == null || LocalDateTime.now().isAfter(employee.getResetTokenExpiry())) {
            throw new NoDataExist("Reset link has expired. Please request a new one.");
        }

        if (!newPassword.equals(confirmPassword)) {
            throw new NoDataExist("Password and Confirm Password do not match");
        }

        if (passwordEncoder.matches(newPassword, employee.getPassword())) {
            throw new NoDataExist("New password cannot be the same as the old password");
        }

        employee.setPassword(passwordEncoder.encode(newPassword));
        employee.setResetToken(null); // clear token so it can't be reused
        employee.setResetTokenExpiry(null);
        Employee saved = this.employeeRepository.save(employee);

        log.info("Password successfully updated for {}", employee.getEmail());

        response.setStatus("SUCCESS");
        response.setStatusCode(AppConstants.OK);
        response.setMessage("Password changed successfully");
        response.setData(this.mapper.map(saved, EmployeeDto.class));
        response.setResponse_message("Process execution completed");

        return response;

    }

}
