package kh.edu.istad.visora.application.dto.template.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import kh.edu.istad.visora.application.dto.backdrop.request.CanvasRequest;
import kh.edu.istad.visora.application.dto.backdrop.request.PageRequest;
import kh.edu.istad.visora.domain.entity.Backdrop;

import java.util.List;
import java.util.Map;

public record UpdateTemplateRequest(

        Integer clientSchemaVersion,

        String name,

        String thumbnail,

        Backdrop.Orientation orientation,

        @NotNull(message = "version is required for optimistic locking")
        Long version,

        @Valid
        CanvasRequest canvas,

        @Valid
        List<PageRequest> pages,

        Map<String, Object> settings,

        String description,

        String categoryUuid,

        String eventType,

        List<String> styles,

        List<String> colors

) {
}