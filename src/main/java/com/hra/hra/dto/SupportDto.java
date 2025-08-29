package com.hra.hra.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class SupportDto {
    private Long id;

    private String issueType;

    private String description;

    private String status;

    private LocalDate createdAt;

    private Long employeeId;
}
