package com.hra.hra.dto;

import com.hra.hra.config.LeaveStatus;
import com.hra.hra.config.LeaveType;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LeaveRequestResponseDto {
    private Long id;
    private Long employeeId;
    private LeaveType leaveType;
    private LocalDate startDate;
    private LocalDate endDate;
    private BigDecimal requestedDays;
    private BigDecimal expectedPaidDays;
    private BigDecimal expectedUnpaidDays;
    private LeaveStatus status;
    private String reason;
}
