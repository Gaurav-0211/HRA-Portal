package com.hra.hra.repository;

import com.hra.hra.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {
    Employee getByEmail(String email);
    List<Employee> findByDepartmentId(Long id);
    Long countByDepartmentId(Long id);

//    @Query("SELECT u FROM Employee u WHERE u.id = i")
//    Employee getById(Long id);
}
