package com.hra.hra.dto;

import com.hra.hra.config.LeaveType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
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

    @NotNull(message = "Leave type cannot be null")
    private LeaveType type;

    @NotBlank(message = "Reason must be added")
    private String reason;
}
