package kh.edu.istad.visora.application.dto.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.Length;

public record RegisterRequest(

        @NotBlank(message = "First name is required")
        @Length(min = 2, message = "First name must be at least 2 characters")
        String firstName,

        @NotBlank(message = "Last name is required")
        @Length(min = 2, message = "Last name must be at least 2 characters")
        String lastName,

        @NotBlank(message = "Username is required")
        @Length(min = 5, message = "Username must be at least 5 characters")
        String username,

        @NotBlank(message = "Email is required")
        @Email(message = "Invalid email format")
        String email,

        @NotBlank(message = "Phone number is required")
        String phoneNumber,

        @NotBlank(message = "Password is required")
        @Length(min = 8, message = "Password must be at least 8 characters")
        String password,

        @NotBlank(message = "Confirm password is required")
        String confirmPassword

) {
}