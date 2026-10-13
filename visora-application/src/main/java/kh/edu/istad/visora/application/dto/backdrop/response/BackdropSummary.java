package kh.edu.istad.visora.application.dto.backdrop.response;

import kh.edu.istad.visora.domain.entity.Backdrop;

import java.sql.Timestamp;

public record BackdropSummary(

        String uuid,

        String ownerUuid,

        String sourceTemplateUuid,

        String name,

        String thumbnail,

        Backdrop.Orientation orientation,

        int pageCount,

        boolean hasTimer,

        boolean canExport,

        Backdrop.Status status,

        long version,

        Timestamp lastOpenedAt,

        Timestamp lastSavedAt,

        Timestamp createdAt,

        Timestamp updatedAt

) {
}