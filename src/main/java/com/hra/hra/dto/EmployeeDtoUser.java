package com.hra.hra.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
public class EmployeeDtoUser {
    private Long id;

    @NotEmpty(message = "Name cannot be null/blank")
    @Size(min = 2, max = 20, message = "Name must be in range 2 - 20 characters")
    private String name;

    @NotEmpty(message = "Contact number cannot be empty")
    @Pattern(regexp = "^[0-9]{10}$", message = "Contact number must be exactly 10 digits")
    private String contactNumber;

    @NotEmpty(message = "Please enter full address")
    private String address;

    private LocalDate joiningData;

    private LocalDate dob;

    private String bloodGroup;

}
