package com.hra.hra.dto;

import com.hra.hra.config.LeaveType;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
// This DTO will be used by employee while applying leave
@Getter
@Setter
@NoArgsConstructor
public class LeaveApplyRequestDto {
    private LocalDate startDate;
    private LocalDate endDate;

    @NotEmpty(message = "Leave type cannot be empty")
    private LeaveType type;

    @NotEmpty(message = "Reason must be added")
    private String reason;
}
