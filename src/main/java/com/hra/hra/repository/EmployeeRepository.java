package com.hra.hra.repository;

import com.hra.hra.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {
    Employee getByEmail(String email);

//    @Query("SELECT u FROM Employee u WHERE u.id = i")
//    Employee getById(Long id);
}
