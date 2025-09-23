package com.hra.hra.repository;

import com.hra.hra.entity.Project;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProjectRepository extends JpaRepository<Project, Long> {
    List<Project> findByEmployees_Id(Long employeeId);

}
