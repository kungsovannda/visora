package kh.edu.istad.visora.application.dto.backdrop.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;

import java.util.Map;

public record ComponentRequest(

        long id,

        @NotBlank(message = "component uuid is required")
        String uuid,

        @NotBlank(message = "type is required")
        String type,

        String content,

        String shape,

        @Valid
        PositionRequest position,

        @Valid
        SizeRequest size,

        Double rotation,

        Integer layerIndex,

        boolean locked,

        boolean visible,

        String image,

        Object vector,

        String name,

        @Valid
        SizeRequest lockAspect,

        String groupUuid,

        boolean flipX,

        boolean flipY,

        String morphId,

        String pageNumber,

        Map<String, Object> animation,

        Map<String, Object> styles,

        Map<String, Object> timer

) {
}