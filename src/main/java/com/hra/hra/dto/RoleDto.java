package com.hra.hra.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Setter
@Getter
public class RoleDto {

    @NotEmpty(message = "Name cannot be empty")
    private String roleName;

    @NotEmpty(message = "Description cannot be empty")
    private String description;
}
