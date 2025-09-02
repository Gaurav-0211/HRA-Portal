package com.hra.hra.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter

public class LoginRequest {

    @Email
    @NotEmpty(message = "Email is required")
    private String email;

    @NotEmpty(message = "Enter a secure password")
    @Pattern(
            regexp = "^(?=.*[A-Z])(?=.*[a-z])(?=.*[0-9])(?=.*[@#&]).{6,}$",
            message = "Password must be at least 6 characters and include uppercase, lowercase, digit and special character (@, #, &)"
    )
    private String password;
}