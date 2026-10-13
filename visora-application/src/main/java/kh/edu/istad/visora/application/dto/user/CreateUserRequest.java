package kh.edu.istad.visora.application.dto.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;

public record CreateUserRequest(

        @Length(min = 5, message = "Username must be at least 5 characters")
        String username,

        @NotBlank(message = "Family name is required")
        @Length(min = 2, message = "Family name must be at least 2 characters")
        String familyName,

        @NotBlank(message = "Given name is required")
        @Length(min = 2, message = "Give name must be at least 2 characters")
        String givenName,

        @NotBlank(message = "Gender is required")
        @Length(min = 4, message = "Gender must be at least 4 characters")
        String gender,

        @NotBlank(message = "Message is required")
        @Email(message = "Invalid email format")
        String email,

        @NotNull(message = "Email verified is required")
        boolean emailVerified,

        String phoneNumber,

        String picture

) {
}
