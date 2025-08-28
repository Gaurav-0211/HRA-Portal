package com.hra.hra.dto;


import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDate;
import java.util.Set;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Builder
public class ProjectDto {
    private Long id;

    @NotEmpty(message = "Project name cannot be empty")
    private String name;

    @NotEmpty(message = "Description cannot be empty")
    private String description;

    @NotNull(message = "Date should be in YYYY-MM-DD Format")
    private LocalDate startDate;

    @NotNull(message = "Date Should be in YYYY-MM-DD format")
    private LocalDate endDate;

    @NotEmpty(message = "Status cannot be blank")
    private String status;


}
