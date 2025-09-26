package com.hra.hra.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class LeaveHistoryDto {
    private Long requestId;
    private String leaveType;
    private LocalDate startDate;
    private LocalDate endDate;
    private double requestedDays;
    private double paidDays;
    private double unpaidDays;
    private String status;
    private String reason;
}
