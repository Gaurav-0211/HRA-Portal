package com.hra.hra.service;

import com.hra.hra.dto.LeaveApplyRequestDto;
import com.hra.hra.dto.LeaveDto;
import com.hra.hra.dto.Response;

import java.util.List;

public interface LeaveService {

    Response applyLeave(Long employeeId, LeaveApplyRequestDto leaveDto);
    Response approveLeave(Long leaveId);
    Response rejectLeave(Long leaveId, String reason);
    Response getLeavesByEmployee(Long employeeId);
    Response getLeaveById(Long id);
    Response deleteLeave(Long id);
    Response getAllLeave(Integer pageNumber, Integer pageSize, String sortBy, String sortDir);
}
