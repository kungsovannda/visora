package kh.edu.istad.visora.application.dto.template.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import kh.edu.istad.visora.application.dto.backdrop.request.CanvasRequest;
import kh.edu.istad.visora.application.dto.backdrop.request.PageRequest;
import kh.edu.istad.visora.domain.entity.Backdrop;

import java.util.List;
import java.util.Map;

public record CreateTemplateRequest(

        Integer clientSchemaVersion,

        @NotBlank(message = "name is required")
        String name,

        String sourceTemplateUuid,

        String thumbnail,

        @NotNull(message = "orientation is required")
        Backdrop.Orientation orientation,

        @NotNull(message = "canvas is required")
        @Valid
        CanvasRequest canvas,

        @NotNull(message = "pages is required")
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