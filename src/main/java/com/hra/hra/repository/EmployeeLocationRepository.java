package com.hra.hra.repository;

import com.hra.hra.entity.EmployeeLocation;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface EmployeeLocationRepository extends JpaRepository<EmployeeLocation, Long> {
    Optional<EmployeeLocation> findTopByEmployeeIdOrderByTimestampDesc(Long employeeId);
}
