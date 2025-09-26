package com.hra.hra.dto;

import com.hra.hra.config.LeaveType;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LeaveBalanceDto {
    private Long employeeId;
    private String fiscalYear;
    private LeaveType leaveType;
    private BigDecimal balance;
}
