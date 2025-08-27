package com.hra.hra.dto;

import com.hra.hra.config.LeaveStatus;
import com.hra.hra.config.LeaveType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
// This DTO will be given as result when anyone try to fetch their leave
@Getter
@Setter
@NoArgsConstructor
public class LeaveDto {

    private Long id;
    private Long employeeId;
    private LocalDate startDate;
    private LocalDate endDate;
    private LeaveType type;
    private LeaveStatus status;
    private String reason;
    private LocalDate appliedOn;
    private LocalDate decisionDate;

}
