package com.hra.hra.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
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

    private String name;

    private String status;

    private String description;

    private LocalDate launchDate;

    @JsonIgnoreProperties("products")
    private Set<EmployeeDto> employees;
}
