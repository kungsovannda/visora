package kh.edu.istad.visora.application.dto.user;

import kh.edu.istad.visora.application.dto.role.RoleSummary;

import java.sql.Timestamp;

public record UserResponse(

        String uuid,

        String username,

        String familyName,

        String givenName,

        String gender,

        String email,

        boolean emailVerified,

        String phoneNumber,

        String picture,

        RoleSummary role,

        boolean isDeleted,

        Timestamp createdAt,

        Timestamp updatedAt,

        String createdBy,

        String updatedBy

) {
}
