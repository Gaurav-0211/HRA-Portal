package com.hra.hra.service.impl;

import com.hra.hra.entity.EmployeeLocation;
import com.hra.hra.repository.EmployeeLocationRepository;
import com.hra.hra.service.EmployeeLocationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
@Slf4j
@Service
@RequiredArgsConstructor
public class EmployeeLocationServiceImpl implements EmployeeLocationService {

    @Autowired
    private final EmployeeLocationRepository locationRepository;

    // API to add current location of an employee to database
    public EmployeeLocation updateLocation(Long employeeId, Double lat, Double lon) {
        log.info("Update location of employee in service Impl triggered");
        EmployeeLocation location = EmployeeLocation.builder()
                .employeeId(employeeId)
                .latitude(lat)
                .longitude(lon)
                .timestamp(LocalDateTime.now())
                .build();
        log.info("Update location of employee in service Impl executed");
        return this.locationRepository.save(location);
    }

    // API to return employee location to frontend
    public Optional<EmployeeLocation> getLatestLocation(Long employeeId) {
        log.info("get employee latest location in service Impl executed");
        return this.locationRepository.findTopByEmployeeIdOrderByTimestampDesc(employeeId);
    }
}
