package kh.edu.istad.visora.application.dto.role;

import java.sql.Timestamp;

public record RoleResponse(

        String uuid,

        String role,

        String description,

        boolean isDeleted,

        Timestamp createdAt,

        Timestamp updatedAt,

        String createdBy,

        String updatedBy

) {
}
