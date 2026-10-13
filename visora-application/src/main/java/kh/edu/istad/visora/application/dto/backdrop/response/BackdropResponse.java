package kh.edu.istad.visora.application.dto.backdrop.response;

import kh.edu.istad.visora.domain.entity.Backdrop;

import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

public record BackdropResponse(

        String uuid,

        String ownerUuid,

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

        long version,

        Timestamp lastOpenedAt,

        Timestamp lastSavedAt,

        Timestamp createdAt,

        Timestamp updatedAt

) {
}