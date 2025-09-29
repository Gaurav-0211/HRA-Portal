package com.hra.hra.dto;

import com.hra.hra.config.LeaveType;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LeaveRequestDto {

    private Long id;

    @NotNull
    private Long employeeId;

    @NotNull
    private LeaveType leaveType;

    @NotNull
    private LocalDate startDate;

    @NotNull
    private LocalDate endDate;

    private String reason;

    private String status;
}
