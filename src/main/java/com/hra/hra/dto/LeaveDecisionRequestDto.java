package com.hra.hra.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

// This DTO will be sent as decision ACCEPTED or REJECTED by the HR/Manager
@Getter
@Setter
@NoArgsConstructor
public class LeaveDecisionRequestDto {

    @NotEmpty(message = "reason cannot be empty")
    private String reason; // Only for rejection
}
