package com.hra.hra.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
@Getter
@Setter
@NoArgsConstructor
public class HolidayDto {
    private String id;

    @NotEmpty(message = "Occasion Cannot be blank")
    private String occasion;

    @NotEmpty(message = "Please enter date format in YYYY-MM-DD")
    private LocalDate date;

    @NotEmpty(message = "Please enter full Day Name")
    private String day;

    @NotEmpty(message = "Enter holiday type")
    private String type;
}
