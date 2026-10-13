package kh.edu.istad.visora.application.dto.role;

import jakarta.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.Length;

public record UpdateRoleRequest(

        @NotBlank(message = "Role name is required")
        @Length(min = 3, message = "Role name must be at least 3 characters")
        String role,

        String description

) {
}
