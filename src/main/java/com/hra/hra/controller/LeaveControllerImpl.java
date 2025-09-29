package com.hra.hra.controller;


import com.hra.hra.dto.LeaveRequestDto;
import com.hra.hra.dto.Response;
import com.hra.hra.service.impl.LeaveServiceImplementation;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/leave")
@RequiredArgsConstructor
public class LeaveControllerImpl {

    @Autowired
    private LeaveServiceImplementation leaveService;


     // Employee applies for leave
    @PostMapping("/request")
    public ResponseEntity<Response> applyLeave(@RequestBody LeaveRequestDto requestDto) {
        Response response = this.leaveService.requestLeave(requestDto);
        return ResponseEntity.ok(response);
    }


     // Approver approves a leave request
    @PostMapping("/approve/leave/{id}/by/{approverId}")
    public ResponseEntity<Response> approveLeave(@PathVariable Long id,
                                                         @PathVariable Long approverId) {
        Response response = leaveService.approveLeave(id, approverId);
        return ResponseEntity.ok(response);
    }


     // Approver rejects a leave request
    @PostMapping("/reject/leave/{id}")
    public ResponseEntity<Response> rejectLeave(@PathVariable Long id,
                                                        @RequestParam Long approverId,
                                                        @RequestParam(required = false) String reason) {
        Response response = this.leaveService.rejectLeave(id, approverId, reason);
        return ResponseEntity.ok(response);
    }


     // Get current leave balances of an employee
    @GetMapping("/balance/{employeeId}/year/{year}")
    public ResponseEntity<Response> getLeaveBalances(@PathVariable Long employeeId,
                                                                  @PathVariable String year) {
        Response balances = this.leaveService.getBalances(employeeId,year);
        return ResponseEntity.ok(balances);
    }


     // Get leave history of an employee
    @GetMapping("/history/{employeeId}")
    public ResponseEntity<Response> getLeaveHistory(@PathVariable Long employeeId) {
        Response history = leaveService.getLeaveHistory(employeeId);
        return ResponseEntity.ok(history);
    }


    // Manually trigger monthly accrual (for testing/admin use)
    @PostMapping("/accrual/run")
    public ResponseEntity<String> runMonthlyAccrual(@RequestParam(required = false) String date) {
        LocalDate runDate = (date == null) ? LocalDate.now() : LocalDate.parse(date);
        this.leaveService.runMonthlyAccrual(runDate);
        return ResponseEntity.ok("Monthly accrual executed for " + runDate);
    }

    // Get all applied Leave
    @GetMapping("/getAll")
    public ResponseEntity<Response> getAllLeave(){
        Response response = this.leaveService.getAllLeave();
        return ResponseEntity.ok(response);
    }
}

