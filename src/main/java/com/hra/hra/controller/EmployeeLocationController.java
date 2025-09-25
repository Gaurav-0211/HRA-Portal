package com.hra.hra.controller;

import com.hra.hra.entity.EmployeeLocation;
import com.hra.hra.exception.NoDataExist;
import com.hra.hra.service.EmployeeLocationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;
@Slf4j
@RestController
@RequestMapping("/api/location")
@RequiredArgsConstructor
public class EmployeeLocationController {

    @Autowired
    private final EmployeeLocationService locationService;


    @Autowired
    private final SimpMessagingTemplate messagingTemplate; // for WebSocket push

    // API for employee app to send location
    @PostMapping("/update")
    public String updateLocation(@RequestParam Long employeeId,
                                 @RequestParam Double latitude,
                                 @RequestParam Double longitude) {
        log.info("update or add live location triggered in controller");
        var saved = this.locationService.updateLocation(employeeId, latitude, longitude);

        // push to subscribers
        this.messagingTemplate.convertAndSend("/topic/employee/" + employeeId, saved);

        log.info("update or add live location in controller executed");

        return "Location updated";
    }

    // API for manager to get latest location of an employee
    @GetMapping("/{employeeId}")
    public EmployeeLocation getLatestLocation(@PathVariable Long employeeId) {
        log.info("get employee location by Id in location Controller");
        return locationService.getLatestLocation(employeeId)
                .orElseThrow(() -> new NoDataExist("No location found"));
    }
}
