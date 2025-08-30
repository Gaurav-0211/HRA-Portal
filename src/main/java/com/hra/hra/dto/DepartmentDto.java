package com.hra.hra.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class DepartmentDto {

    private Long id;

    @NotEmpty(message = "Department Name cannot be empty")
    private String departmentName;

    @NotEmpty(message = "Description cannot be blank")
    private String description;
}
