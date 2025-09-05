package com.hra.hra.dto;

import jakarta.validation.constraints.NotEmpty;
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

    @NotEmpty(message = "Issue type must be filled")
    private String issueType;

    @NotEmpty(message = "description cannot be empty")
    private String description;

    @NotEmpty(message = "Status cannot be empty")
    private String status;

    private LocalDate createdAt;

    private Long employeeId;
}
