package com.hra.hra.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class ResetPasswordRequest {
    @NotEmpty(message = "Enter a secure password")
    @Pattern(
            regexp = "^(?=.*[A-Z])(?=.*[a-z])(?=.*[0-9])(?=.*[@#&]).{6,}$",
            message = "Password must be at least 6 characters and include uppercase, lowercase, digit and special character (@, #, &)"
    )
    private String newPassword;
    private String confirmPassword;
}