package com.hra.hra.controller;


import com.hra.hra.dto.LeaveApplyRequestDto;
import com.hra.hra.dto.LeaveDecisionRequestDto;
import com.hra.hra.dto.Response;
import com.hra.hra.service.LeaveService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
@Slf4j
@RestController
@RequestMapping("/api/leaves")
public class LeaveController {

    @Autowired
    private LeaveService leaveService;

    // POST Request to Apply leave (Employee)
    @PostMapping("/apply/{employeeId}")
    public ResponseEntity<Response> applyLeave(
            @PathVariable Long employeeId,
            @RequestBody @Valid LeaveApplyRequestDto requestDto) {
        log.info("Apply leave in controller");
        Response response = this.leaveService.applyLeave(employeeId, requestDto);
        log.info("Apply leave in controller executed");

        return ResponseEntity.ok(response);
    }

    // PUT Request to Approve leave (Manager/HR)
    @PutMapping("/approve/{leaveId}")
    public ResponseEntity<Response> approveLeave(@PathVariable Long leaveId) {
        log.info("Approve leave in controller");
        Response response = this.leaveService.approveLeave(leaveId);
        log.info("Approve leave in controller executed");

        return ResponseEntity.ok(response);
    }

    //PUT Request to Reject leave (Manager/HR)
    @PutMapping("/reject/{leaveId}")
    public ResponseEntity<Response> rejectLeave(
            @PathVariable Long leaveId,
            @RequestBody @Valid LeaveDecisionRequestDto requestDto) {
        log.info("Reject leave in controller");
        Response response = this.leaveService.rejectLeave(leaveId, requestDto.getReason());
        log.info("Reject leave in controller executed");

        return ResponseEntity.ok(response);
    }

    //  GET Request to Get all leaves for an employee
    @GetMapping("/employee/{employeeId}")
    public ResponseEntity<Response> getLeavesByEmployee(@PathVariable Long employeeId) {
        log.info("Get applied leave by an employee in controller");
        Response response = this.leaveService.getLeavesByEmployee(employeeId);
        log.info("Get applied leave by an employee in controller executed");

        return ResponseEntity.ok(response);
    }

}
