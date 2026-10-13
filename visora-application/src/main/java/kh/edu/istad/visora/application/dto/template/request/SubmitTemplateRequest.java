package kh.edu.istad.visora.application.dto.template.request;

import java.util.List;

public record SubmitTemplateRequest(

        String proposedName,

        String description,

        String categoryUuid,

        String eventType,

        List<String> styles,

        List<String> colors

) {
}