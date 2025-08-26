package com.hra.hra.controller;


import com.hra.hra.dto.LeaveApplyRequestDto;
import com.hra.hra.dto.LeaveDecisionRequestDto;
import com.hra.hra.dto.LeaveDto;
import com.hra.hra.dto.Response;
import com.hra.hra.service.LeaveService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/leaves")
public class LeaveController {

    @Autowired
    private LeaveService leaveService;

    // POST Request to Apply leave (Employee)
    @PostMapping("/apply/{employeeId}")
    public ResponseEntity<Response> applyLeave(
            @PathVariable Long employeeId,
            @RequestBody LeaveApplyRequestDto requestDto) {
        Response response = this.leaveService.applyLeave(employeeId, requestDto);
        return ResponseEntity.ok(response);
    }

    // PUT Request to Approve leave (Manager/HR)
    @PutMapping("/approve/{leaveId}")
    public ResponseEntity<Response> approveLeave(@PathVariable Long leaveId) {
        Response response = this.leaveService.approveLeave(leaveId);
        return ResponseEntity.ok(response);
    }

    //PUT Request to Reject leave (Manager/HR)
    @PutMapping("/reject/{leaveId}")
    public ResponseEntity<Response> rejectLeave(
            @PathVariable Long leaveId,
            @RequestBody LeaveDecisionRequestDto requestDto) {
        Response response = this.leaveService.rejectLeave(leaveId, requestDto.getReason());
        return ResponseEntity.ok(response);
    }

    //  GET Request to Get all leaves for an employee
    @GetMapping("/employee/{employeeId}")
    public ResponseEntity<Response> getLeavesByEmployee(@PathVariable Long employeeId) {
        Response response = this.leaveService.getLeavesByEmployee(employeeId);
        return ResponseEntity.ok(response);
    }

}
