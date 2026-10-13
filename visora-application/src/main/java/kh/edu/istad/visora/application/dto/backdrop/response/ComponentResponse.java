package kh.edu.istad.visora.application.dto.backdrop.response;

import java.util.Map;

public record ComponentResponse(

        long id,

        String uuid,

        String type,

        String content,

        String shape,

        PositionResponse position,

        SizeResponse size,

        Double rotation,

        Integer layerIndex,

        boolean locked,

        boolean visible,

        String image,

        Object vector,

        String name,

        SizeResponse lockAspect,

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