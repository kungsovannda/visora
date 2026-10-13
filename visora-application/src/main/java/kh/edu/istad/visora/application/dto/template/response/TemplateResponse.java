package kh.edu.istad.visora.application.dto.template.response;

import kh.edu.istad.visora.application.dto.backdrop.response.CanvasResponse;
import kh.edu.istad.visora.application.dto.backdrop.response.PageResponse;
import kh.edu.istad.visora.domain.entity.Backdrop;
import kh.edu.istad.visora.domain.enums.TemplateStatus;

import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

public record TemplateResponse(

        String uuid,

        String ownerUuid,

        String sourceBackdropUuid,

        String sourceTemplateUuid,

        String name,

        String thumbnail,

        Backdrop.Orientation orientation,

        CanvasResponse canvas,

        List<PageResponse> pages,

        int pageCount,

        boolean hasTimer,

        boolean canExport,

        Map<String, Object> settings,

        Backdrop.Status status,

        TemplateStatus templateStatus,

        String proposedName,

        String description,

        String categoryUuid,

        String eventType,

        List<String> styles,

        List<String> colors,

        String submittedBy,

        Timestamp submittedAt,

        long version,

        Timestamp lastOpenedAt,

        Timestamp lastSavedAt,

        Timestamp createdAt,

        Timestamp updatedAt

) {
}