package com.hra.hra.service;

import com.hra.hra.dto.ProjectDto;
import com.hra.hra.dto.Response;

import java.util.List;

public interface ProjectService {
    Response createProject(ProjectDto projectDto);
    Response assignEmployeeToProject(Long projectId, Long employeeId);
    Response removeEmployeeFromProject(Long projectId, Long employeeId);
    Response getProjectById(Long projectId);
    Response getAllProjects();
    Response updateProject(Long id, ProjectDto projectDto);
    Response deleteProject(Long id);
}
