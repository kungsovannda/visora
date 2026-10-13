package kh.edu.istad.visora.application.dto.role;

import jakarta.validation.constraints.NotBlank;

public record AssignRoleRequest(

        @NotBlank(message = "Role UUID is required")
        String roleUuid

) {
}
