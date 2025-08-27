package com.hra.hra.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

// This DTO will be sent as decision ACCEPTED or REJECTED by the HR/Manager
@Getter
@Setter
@NoArgsConstructor
public class LeaveDecisionRequestDto {

    private String reason; // Only for rejection
}
