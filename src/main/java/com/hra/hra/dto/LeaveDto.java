package com.hra.hra.dto;

import com.hra.hra.config.LeaveStatus;
import com.hra.hra.config.LeaveType;
import jakarta.validation.constraints.NotEmpty;
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

    @NotEmpty(message = "Employee Id cannot be empty")
    private Long employeeId;

    @NotEmpty(message = "Date should be in YYYY-MM-DD format")
    private LocalDate startDate;

    @NotEmpty(message = "Date should be in YYYY-MM-DD format")
    private LocalDate endDate;

    private LeaveType type;
    private LeaveStatus status;

    @NotEmpty(message = "Leave reason cannot be empty")
    private String reason;

    private LocalDate appliedOn;
    private LocalDate decisionDate;

}
