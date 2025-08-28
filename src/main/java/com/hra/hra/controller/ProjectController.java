package com.hra.hra.controller;

import com.hra.hra.dto.ProjectDto;
import com.hra.hra.dto.Response;
import com.hra.hra.service.ProjectService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/projects")
public class ProjectController {

    @Autowired
    private ProjectService projectService;

    // POST Request to crate a new Project
    @PostMapping("/createProject")
    public ResponseEntity<Response> createProject(@RequestBody @Valid ProjectDto projectDto){
        Response response = this.projectService.createProject(projectDto);

        return ResponseEntity.ok(response);
    }


    // POST Request to assign project to an employee
    @PostMapping("/{projectId}/assign-employee/{employeeId}")
    public ResponseEntity<Response> assignProject(@PathVariable Long projectId,
                                                  @PathVariable Long employeeId){
        Response response = this.projectService.assignEmployeeToProject(projectId, employeeId);

        return ResponseEntity.ok(response);
    }

    // DELETE request to remove project from an employee
    @DeleteMapping("/{projectId}/remove-employee/{employeeId}")
    public ResponseEntity<Response> removeProject(@PathVariable Long projectId,
                                                  @PathVariable Long employeeId){
        Response response = this.projectService.removeEmployeeFromProject(projectId, employeeId);

        return ResponseEntity.ok(response);
    }

    // GET Request to fetch a single project By id
    @GetMapping("/getById/{id}")
    public ResponseEntity<Response> getByIdProject(@PathVariable Long id){
        Response response = this.projectService.getProjectById(id);

        return ResponseEntity.ok(response);
    }

    // GET Request to fetch all project
    @GetMapping("/getAll")
    public ResponseEntity<Response> getAllProject(){
        Response response = this.projectService.getAllProjects();

        return ResponseEntity.ok(response);
    }

    // PUT request to update an existing project
    @PutMapping("/updateProject/{id}")
    public ResponseEntity<Response> updateProject(@PathVariable Long id, @RequestBody ProjectDto projectDto){
        Response response = this.projectService.updateProject(id,projectDto);

        return ResponseEntity.ok(response);
    }

    // DELETE request to delete project
    @GetMapping("/deleteProject/{id}")
    public ResponseEntity<Response> deleteProject(@PathVariable Long id){
        Response response = this.projectService.deleteProject(id);

        return ResponseEntity.ok(response);
    }

}
