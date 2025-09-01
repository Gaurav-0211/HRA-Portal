package com.hra.hra.controller;

import com.hra.hra.config.AppConstants;
import com.hra.hra.dto.ProjectDto;
import com.hra.hra.dto.Response;
import com.hra.hra.service.ProjectService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
@Slf4j
@RestController
@RequestMapping("/api/projects")
public class ProjectController {

    @Autowired
    private ProjectService projectService;

    // POST Request to crate a new Project
    @PostMapping("/createProject")
    public ResponseEntity<Response> createProject(@RequestBody @Valid ProjectDto projectDto){
        log.info("Create Project in controller");
        Response response = this.projectService.createProject(projectDto);
        log.info("Create Project in controller executed");

        return ResponseEntity.ok(response);
    }


    // POST Request to assign project to an employee
    @PostMapping("/{projectId}/assign-employee/{employeeId}")
    public ResponseEntity<Response> assignProject(@PathVariable Long projectId,
                                                  @PathVariable Long employeeId){
        log.info("Assign project to an employee in controller");
        Response response = this.projectService.assignEmployeeToProject(projectId, employeeId);
        log.info("Assign project to an employee in controller executed");

        return ResponseEntity.ok(response);
    }

    // DELETE request to remove project from an employee
    @DeleteMapping("/{projectId}/remove-employee/{employeeId}")
    public ResponseEntity<Response> removeProject(@PathVariable Long projectId,
                                                  @PathVariable Long employeeId){
        log.info("Remove project from an employee in controller");
        Response response = this.projectService.removeEmployeeFromProject(projectId, employeeId);
        log.info("Remove project from an employee in controller executed");

        return ResponseEntity.ok(response);
    }

    // GET Request to fetch a single project By id
    @GetMapping("/getById/{id}")
    public ResponseEntity<Response> getByIdProject(@PathVariable Long id){
        log.info("Get project by id in controller");
        Response response = this.projectService.getProjectById(id);
        log.info("Get project by id in controller executed");

        return ResponseEntity.ok(response);
    }

    // GET Request to fetch all project
    @GetMapping("/getAll")
    public ResponseEntity<Response> getAllProject(
            @RequestParam(value = "pageNumber", defaultValue = AppConstants.PAGE_NUMBER, required = false) Integer pageNumber,
            @RequestParam(value = "pageSize", defaultValue = AppConstants.PAGE_SIZE, required = false) Integer pageSize,
            @RequestParam(value = "sortBy", defaultValue = AppConstants.SORT_BY, required = false) String sortBy,
            @RequestParam(value = "sortDir", defaultValue = AppConstants.SORT_DIR, required = false) String sortDir
    ){
        log.info("Get All project in controller");
        Response response = this.projectService.getAllProjects(pageNumber, pageSize, sortBy, sortDir);
        log.info("Get all project in controller executed");

        return ResponseEntity.ok(response);
    }

    // PUT request to update an existing project
    @PutMapping("/updateProject/{id}")
    public ResponseEntity<Response> updateProject(@PathVariable Long id,@Valid @RequestBody ProjectDto projectDto){
        log.info("Update project by id in controller");
        Response response = this.projectService.updateProject(id,projectDto);
        log.info("Update project by id in controller executed");

        return ResponseEntity.ok(response);
    }

    // DELETE request to delete project
    @GetMapping("/deleteProject/{id}")
    public ResponseEntity<Response> deleteProject(@PathVariable Long id){
        log.info("Delete project by id in controller");
        Response response = this.projectService.deleteProject(id);
        log.info("delete project by id in controller executed");

        return ResponseEntity.ok(response);
    }

}
