package com.hra.hra.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.Set;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ProductDto {
    private Long id;

    @NotEmpty(message = "Product name cannot be empty")
    private String name;

    @NotEmpty(message = "Product status cannot be empty")
    private String status;

    @NotEmpty(message = "Description must be filled")
    private String description;

    private LocalDate launchDate;

    @JsonIgnoreProperties("products")
    private Set<EmployeeDto> employees;
}
