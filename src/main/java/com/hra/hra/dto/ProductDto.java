package com.hra.hra.dto;

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

    private Set<Long> employeeIds;
}
