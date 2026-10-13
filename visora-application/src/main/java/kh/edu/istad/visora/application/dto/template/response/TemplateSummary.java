package kh.edu.istad.visora.application.dto.template.response;

import kh.edu.istad.visora.domain.entity.Backdrop;
import kh.edu.istad.visora.domain.enums.TemplateStatus;

import java.sql.Timestamp;

public record TemplateSummary(

        String uuid,

        String ownerUuid,

        String sourceBackdropUuid,

        String sourceTemplateUuid,

        String name,

        String description,

        String thumbnail,

        Backdrop.Orientation orientation,

        int pageCount,

        boolean hasTimer,

        boolean canExport,

        Backdrop.Status status,

        TemplateStatus templateStatus,

        String submittedBy,

        String categoryUuid,

        String eventType,

        Timestamp submittedAt,

        long version,

        Timestamp lastOpenedAt,

        Timestamp lastSavedAt,

        Timestamp createdAt,

        Timestamp updatedAt

) {
}