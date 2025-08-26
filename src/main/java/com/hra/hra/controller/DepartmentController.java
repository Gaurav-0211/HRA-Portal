package com.hra.hra.controller;

import com.hra.hra.dto.DepartmentDto;
import com.hra.hra.dto.Response;
import com.hra.hra.service.DepartmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/departments")
public class DepartmentController {

    @Autowired
    private DepartmentService service;

    // POST Request to add department
    @PostMapping("/addDepartment")
    public ResponseEntity<Response> addDepartment(@RequestBody DepartmentDto departmentDto){
        Response response = this.service.addDepartment(departmentDto);
        return ResponseEntity.ok(response);
    }

    // GET Request to fetch all departments
    @GetMapping("/getDepartments")
    public ResponseEntity<Response> getAllDepartment(){
        Response response = this.service.getAllDepartment();

        return ResponseEntity.ok(response);
    }

    // DELETE Request to delete a department
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Response> deleteDepartment(@PathVariable Long id){
        Response response = this.service.deleteDepartment(id);

        return ResponseEntity.ok(response);
    }

    // PUT Request to update an existing department
    @PutMapping("/updateDepartment")
    ResponseEntity<Response> updateDept(@RequestBody DepartmentDto departmentDto){
        Response response = this.service.updateDepartment(departmentDto);

        return ResponseEntity.ok(response);
    }

}
