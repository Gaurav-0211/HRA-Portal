package com.hra.hra.service.impl;

import com.hra.hra.config.AppConstants;
import com.hra.hra.dto.ProjectDto;
import com.hra.hra.dto.Response;
import com.hra.hra.entity.Employee;
import com.hra.hra.entity.Project;
import com.hra.hra.exception.NoDataExist;
import com.hra.hra.repository.EmployeeRepository;
import com.hra.hra.repository.ProjectRepository;
import com.hra.hra.service.ProjectService;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class ProjectServiceImpl implements ProjectService {

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private ModelMapper mapper;

    @Autowired
    private Response response;

    // API to add a new project
    @Override
    public Response createProject(ProjectDto projectDto) {
        log.info("Add project service Impl triggered");
        Project project = this.mapper.map(projectDto, Project.class);
        this.projectRepository.save(project);
        response.setStatus("SUCCESS");
        response.setMessage("Project created successfully");
        response.setData(this.mapper.map(project, ProjectDto.class));
        response.setStatusCode(AppConstants.CREATED);
        response.setResponse_message("Process executed completed");
        return response;
    }

    // API to assign a project to an employee
    @Override
    public Response assignEmployeeToProject(Long projectId, Long employeeId) {
        log.info("Assign project to employee service impl");
        Project project = this.projectRepository.findById(projectId)
                .orElseThrow(() -> new NoDataExist("Project not found"));

        Employee employee = this.employeeRepository.findById(employeeId)
                .orElseThrow(() -> new NoDataExist("Employee not found"));

        project.getEmployees().add(employee);

        Project updated = this.projectRepository.save(project);

        response.setStatus("SUCCESS");
        response.setMessage("Project assigned to employee successfully");
        response.setData(this.mapper.map(updated, ProjectDto.class));
        response.setStatusCode(AppConstants.OK);
        response.setResponse_message("Process executed completed");
        return response;
    }

    // Remove project from an employee
    @Override
    public Response removeEmployeeFromProject(Long projectId, Long employeeId) {
        log.info("Remove project from an employee service Impl");
        Project project = this.projectRepository.findById(projectId)
                .orElseThrow(() -> new NoDataExist("Project not found"));

        Employee employee = this.employeeRepository.findById(employeeId)
                .orElseThrow(() -> new NoDataExist("Employee not found"));

        project.getEmployees().remove(employee);

        Project updated = this.projectRepository.save(project);

        response.setStatus("SUCCESS");
        response.setMessage("Project removed from employee");
        response.setData(this.mapper.map(updated, ProjectDto.class));
        response.setStatusCode(AppConstants.OK);
        response.setResponse_message("Process executed completed");
        return response;

    }

    // API to get all project by project ID
    @Override
    public Response getProjectById(Long projectId) {
        log.info("Get project by id project service Impl");
        Project project = this.projectRepository.findById(projectId)
                .orElseThrow(() -> new NoDataExist("Project not found"));

        response.setStatus("SUCCESS");
        response.setMessage("Project fetched success");
        response.setData(this.mapper.map(project, ProjectDto.class));
        response.setStatusCode(AppConstants.OK);
        response.setResponse_message("Process executed completed");
        return response;
    }

    // APiI to get all projects
    @Override
    public Response getAllProjects() {
        log.info("Get all projects service Impl");
        List<Project> projects = this.projectRepository.findAll();

        response.setStatus("SUCCESS");
        response.setMessage("All Project fetched success");
        response.setData(projects.stream().map((p)->this.mapper.map(p, ProjectDto.class)).collect(Collectors.toList()));
        response.setStatusCode(AppConstants.OK);
        response.setResponse_message("Process executed completed");
        return response;
    }

    // API to update project record
    @Override
    public Response updateProject(Long id, ProjectDto projectDto) {
        log.info("update an existing project service Impl");
        Project project = this.projectRepository.findById(id)
                .orElseThrow(()->new NoDataExist("No project found with given ID"));
        this.mapper.map(projectDto, Project.class);
        Project saved = this.projectRepository.save(project);

        response.setStatus("SUCCESS");
        response.setMessage("Project Updated success");
        response.setData(this.mapper.map(saved, ProjectDto.class));
        response.setStatusCode(AppConstants.OK);
        response.setResponse_message("Process executed completed");
        return response;
    }

    // API to delete an existing project
    @Override
    public Response deleteProject(Long id) {
        log.info("Deleted an existing project service impl");
        Project project = this.projectRepository.findById(id)
                .orElseThrow(()-> new NoDataExist("No project found with given Id"));
        this.projectRepository.delete(project);

        response.setStatus("SUCCESS");
        response.setMessage("Project Deleted success");
        response.setData(null);
        response.setStatusCode(AppConstants.OK);
        response.setResponse_message("Process executed completed");
        return response;
    }
}
