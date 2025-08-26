package com.hra.hra.service.impl;

import com.hra.hra.config.AppConstants;
import com.hra.hra.config.LeaveStatus;
import com.hra.hra.dto.LeaveApplyRequestDto;
import com.hra.hra.dto.LeaveDto;
import com.hra.hra.dto.Response;
import com.hra.hra.entity.Employee;
import com.hra.hra.entity.Leave;
import com.hra.hra.exception.NoEmployeeExist;
import com.hra.hra.exception.NoLeaveExist;
import com.hra.hra.repository.EmployeeRepository;
import com.hra.hra.repository.LeaveRepository;
import com.hra.hra.service.LeaveService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class LeaveServiceImpl implements LeaveService {

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private LeaveRepository leaveRepository;

    @Autowired
    private ModelMapper mapper;

    @Autowired
    private Response response;

    // API to apply Leave with employee Id
    @Override
    public Response applyLeave(Long employeeId, LeaveApplyRequestDto leaveDto) {
        Employee employee = this.employeeRepository.findById(employeeId)
                .orElseThrow(() -> new NoEmployeeExist("Employee not found"));

        Leave leave = this.mapper.map(leaveDto, Leave.class);

        //  Set relationships & defaults
        leave.setEmployee(employee);
        leave.setStatus(LeaveStatus.PENDING);
        leave.setAppliedOn(LocalDate.now());

        Leave savedLeave = this.leaveRepository.save(leave);

        // Returned a response
        response.setStatus("SUCCESS");
        response.setMessage("Leave applied successfully");
        response.setData(mapper.map(savedLeave, LeaveDto.class));
        response.setStatusCode(AppConstants.CREATED);
        response.setResponse_message("Process Execution success");
        return response;
    }

    // API to approve leave By other authority(HR, Manager)
    @Override
    public Response approveLeave(Long leaveId) {
        Leave leave = this.leaveRepository.findById(leaveId)
                .orElseThrow(() -> new NoLeaveExist("Leave not found"));

        leave.setStatus(LeaveStatus.APPROVED);
        leave.setDecisionDate(LocalDate.now());

        response.setStatus("SUCCESS");
        response.setMessage("Leave approved Successfully");
        response.setData(mapper.map(this.leaveRepository.save(leave), LeaveDto.class));
        response.setStatusCode(AppConstants.OK);
        response.setResponse_message("Process Execution success");

        return response;
    }

    // API to reject Leave by authorize person (HR/Manager)
    @Override
    public Response rejectLeave(Long leaveId, String reason) {
        Leave leave = this.leaveRepository.findById(leaveId)
                .orElseThrow(() -> new NoLeaveExist("Leave not found"));

        leave.setStatus(LeaveStatus.REJECTED);
        leave.setReason(reason);  // update rejection reason
        leave.setDecisionDate(LocalDate.now());

        response.setStatus("SUCCESS");
        response.setMessage("Leave Rejected!!!");
        response.setData(mapper.map(this.leaveRepository.save(leave), LeaveDto.class));
        response.setStatusCode(AppConstants.OK);
        response.setResponse_message("Process Execution success");

        return response;
    }

    // API to get all leaves of an employee
    @Override
    public Response getLeavesByEmployee(Long employeeId) {
        List<Leave> leaves = this.leaveRepository.findByEmployeeId(employeeId);

        response.setStatus("SUCCESS");
        response.setMessage("All Leaves Fetched of employee");
        response.setData(leaves.stream().map((l) -> mapper.map(l, LeaveDto.class)).collect(Collectors.toList()));
        response.setStatusCode(AppConstants.OK);
        response.setResponse_message("Process Execution success");
        return response;
    }

}
