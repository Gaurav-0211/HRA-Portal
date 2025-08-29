package com.hra.hra.repository;

import com.hra.hra.entity.Support;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SupportRepository extends JpaRepository<Support, Long> {
    List<Support> findByEmployeeId(Long employeeId);
}
