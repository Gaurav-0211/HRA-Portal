package com.hra.hra.service.impl;

import com.hra.hra.config.AppConstants;
import com.hra.hra.config.LeaveStatus;
import com.hra.hra.dto.*;
import com.hra.hra.entity.Employee;
import com.hra.hra.entity.Leave;
import com.hra.hra.exception.NoDataExist;
import com.hra.hra.exception.NoLeaveExist;
import com.hra.hra.repository.EmployeeRepository;
import com.hra.hra.repository.LeaveRepository;
import com.hra.hra.service.LeaveService;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;
@Slf4j
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

    // API to apply Leave with employee ID
    @Override
    public Response applyLeave(Long employeeId, LeaveApplyRequestDto leaveDto) {
        log.info("Apply leave in service impl triggered");
        Employee employee = this.employeeRepository.findById(employeeId)
                .orElseThrow(() -> new NoDataExist("Employee not found"));

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
        log.info("Apply leave in service impl executed");

        return response;
    }

    // API to approve leave By other authority(HR, Manager)
    @Override
    public Response approveLeave(Long leaveId) {
        log.info("Approve leave in service impl triggered");

        Leave leave = this.leaveRepository.findById(leaveId)
                .orElseThrow(() -> new NoLeaveExist("Leave not found"));

        leave.setStatus(LeaveStatus.APPROVED);
        leave.setDecisionDate(LocalDate.now());

        response.setStatus("SUCCESS");
        response.setMessage("Leave approved Successfully");
        response.setData(mapper.map(this.leaveRepository.save(leave), LeaveDto.class));
        response.setStatusCode(AppConstants.OK);
        response.setResponse_message("Process Execution success");
        log.info("Approve leave in service impl executed");

        return response;
    }

    // API to reject Leave by authorize person (HR/Manager)
    @Override
    public Response rejectLeave(Long leaveId, String reason) {
        log.info("Reject leave in service impl triggered");

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
        log.info("Reject leave in service impl executed");

        return response;
    }

    // API to get all leaves of an employee
    @Override
    public Response getLeavesByEmployee(Long employeeId) {
        log.info("Get Leave by an employee in service impl triggered");

        List<Leave> leaves = this.leaveRepository.findByEmployeeId(employeeId);

        response.setStatus("SUCCESS");
        response.setMessage("All Leaves Fetched of employee");
        response.setData(leaves.stream().map((l) -> mapper.map(l, LeaveDto.class)).collect(Collectors.toList()));
        response.setStatusCode(AppConstants.OK);
        response.setResponse_message("Process Execution success");
        log.info("Get Leave by an employee in service impl executed");

        return response;
    }

    // API to fetch single leave by ID
    @Override
    public Response getLeaveById(Long id) {
        log.info("Get leave by id in leave service Impl");
        Leave leave = this.leaveRepository.findById(id)
                .orElseThrow(() -> new NoDataExist("No Leave found with given Id" + id));

        response.setStatus("SUCCESS");
        response.setMessage("Leave Fetched successfully");
        response.setData(this.mapper.map(leave, LeaveDto.class));
        response.setStatusCode(AppConstants.OK);
        response.setResponse_message("Process Execution success");
        log.info("Get leave by id in leave service Impl executed");

        return response;
    }

    // API to delete a leave
    @Override
    public Response deleteLeave(Long id) {
        log.info("Delete leave by id in service Impl");
       Leave leave = this.leaveRepository.findById(id)
               .orElseThrow(()-> new NoDataExist("No leave found with given ID "+id));

       this.leaveRepository.delete(leave);

        response.setStatus("SUCCESS");
        response.setMessage("leave deleted successfully");
        response.setData(null);
        response.setStatusCode(AppConstants.OK);
        response.setResponse_message("Process Execution success");
        log.info("Delete leave by ID in service Impl executed");

        return response;
    }

    // API to get all Leave
    @Override
    public Response getAllLeave(Integer pageNumber, Integer pageSize, String sortBy, String sortDir) {
        log.info("Get all leave in leave service Impl");
        Sort sort = sortDir != null && sortDir.equalsIgnoreCase("asc")
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);
        Page<Leave> leavePage = this.leaveRepository.findAll(pageable);

        List<LeaveDto> leaveDtoList = leavePage
                .getContent()
                .stream()
                .map(leave -> mapper.map(leave, LeaveDto.class))
                .collect(Collectors.toList());

        log.info("All Leave converted into pages in employee service impl");
        PageResponse<LeaveDto> obj = PageResponse.<LeaveDto>builder()
                .content(leaveDtoList)
                .pageNumber(leavePage.getNumber())
                .pageSize(leavePage.getSize())
                .totalElements(leavePage.getTotalElements())
                .totalPage(leavePage.getTotalPages())
                .lastPage(leavePage.isLast())
                .build();

        response.setStatus("SUCCESS");
        response.setMessage("All Leave fetched successfully");
        response.setData(obj);
        response.setStatusCode(AppConstants.OK);
        response.setResponse_message("Execution process completed");
        log.info("Get all leave in service Impl executed");

        return response;

    }
}
