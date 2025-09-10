package com.hra.hra.service;

import com.hra.hra.entity.EmployeeLocation;

import java.util.Optional;

public interface EmployeeLocationService {
    EmployeeLocation updateLocation(Long employeeId, Double lat, Double lon);
    Optional<EmployeeLocation> getLatestLocation(Long employeeId);

}
