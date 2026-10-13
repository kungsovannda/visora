package kh.edu.istad.visora.application.dto.backdrop.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.List;
import java.util.Map;

public record PageRequest(

        long id,

        @NotBlank(message = "page uuid is required")
        String uuid,

        String name,

        @NotNull(message = "pageNumber is required")
        @Min(value = 1, message = "pageNumber must be at least 1")
        Integer pageNumber,

        @Valid
        List<GroupRequest> groups,

        Map<String, Object> background,

        @Valid
        List<AnimationRequest> animations,

        @Valid
        TransitionRequest transition,

        @Valid
        List<ComponentRequest> components

) {
}