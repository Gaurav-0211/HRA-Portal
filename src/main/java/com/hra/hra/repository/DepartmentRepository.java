package com.hra.hra.repository;

import com.hra.hra.entity.Department;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface DepartmentRepository extends JpaRepository<Department, Long> {

    @Query("SELECT u FROM Department u WHERE u.departmentName = :name")
    Department getByDepartmentName(String name);
}
