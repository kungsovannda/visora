package kh.edu.istad.visora.application.dto.category;

import java.sql.Timestamp;

public record CategoryResponse(

        String uuid,

        String name,

        String slug,

        String description,

        String icon,

        Integer displayOrder,

        boolean isActive,

        long templateCount,

        Timestamp createdAt,

        Timestamp updatedAt,

        String createdBy,

        String updatedBy
) {
}