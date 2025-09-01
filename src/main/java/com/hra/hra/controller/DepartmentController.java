package com.hra.hra.controller;

import com.hra.hra.config.AppConstants;
import com.hra.hra.dto.DepartmentDto;
import com.hra.hra.dto.Response;
import com.hra.hra.service.DepartmentService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/departments")
public class DepartmentController {

    @Autowired
    private DepartmentService service;

    // POST Request to add department
    @PostMapping("/addDepartment")
    public ResponseEntity<Response> addDepartment(@Valid @RequestBody DepartmentDto departmentDto){
        log.info("Add new Department in controller");
        Response response = this.service.addDepartment(departmentDto);
        return ResponseEntity.ok(response);
    }

    // GET Request to fetch all departments
    @GetMapping("/getDepartments")
    public ResponseEntity<Response> getAllDepartment(
            @RequestParam(value = "pageNumber", defaultValue = AppConstants.PAGE_NUMBER, required = false) Integer pageNumber,
            @RequestParam(value = "pageSize", defaultValue = AppConstants.PAGE_SIZE, required = false) Integer pageSize,
            @RequestParam(value = "sortBy", defaultValue = AppConstants.SORT_BY, required = false) String sortBy,
            @RequestParam(value = "sortDir", defaultValue = AppConstants.SORT_DIR, required = false) String sortDir
    ){
        log.info("Get all department in controller");
        Response response = this.service.getAllDepartment(pageNumber,pageSize,sortBy,sortDir);

        return ResponseEntity.ok(response);
    }

    // DELETE Request to delete a department
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Response> deleteDepartment(@PathVariable Long id){
        log.info("delete department in controller");
        Response response = this.service.deleteDepartment(id);

        return ResponseEntity.ok(response);
    }

    // PUT Request to update an existing department
    @PutMapping("/updateDepartment/{id}")
    ResponseEntity<Response> updateDept(@PathVariable Long id,@Valid @RequestBody DepartmentDto departmentDto){
        log.info("update department in controller");
        Response response = this.service.updateDepartment(id,departmentDto);

        return ResponseEntity.ok(response);
    }

    // GET request to fetch a single department by ID
    @GetMapping("/getByDepartmentId/{id}")
    public  ResponseEntity<Response> getById(@PathVariable Long id){
        log.info("Get Department by id in controller");
        Response response = this.service.getDepartmentById(id);
        log.info("Get Department by id in controller executed");

        return ResponseEntity.ok(response);
    }

    // GET request to fetch all employees department wise
    @GetMapping("/getAllEmployeeByDepartmentId/{id}")
    public  ResponseEntity<Response> getEmployeeByDepartment(@PathVariable Long id){
        log.info("Get all employee in department in controller");
        Response response = this.service.getEmployeesOfDepartment(id);
        log.info("Get all employee in department in controller executed");

        return ResponseEntity.ok(response);
    }

    @GetMapping("/getEmployeeCountDepartmentId/{id}")
    public  ResponseEntity<Response> getEmployeeCountByDepartment(@PathVariable Long id){
        log.info("Get all employee count department wise in controller");
        Response response = this.service.getDepartmentAndEmployeeCount(id);
        log.info("Get all employee count department wise in controller executed");

        return ResponseEntity.ok(response);
    }

    // GET Request to fetch employee by department name
    @GetMapping("/getDepartmentByName/{name}")
    public ResponseEntity<Response> getEmployeeCountByDepartment(@PathVariable String name){
        log.info("Get all department by name in controller");
        Response response = this.service.getDepartmentByName(name);
        log.info("Get all department by name in controller executed");

        return ResponseEntity.ok(response);
    }

}
