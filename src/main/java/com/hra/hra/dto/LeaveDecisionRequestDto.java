package com.hra.hra.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class LeaveDecisionRequestDto {

    private String reason; // Only for rejection
}
