package kh.edu.istad.visora.application.dto.backdrop.response;

import java.util.List;
import java.util.Map;

public record PageResponse(

        long id,

        String uuid,

        String name,

        int pageNumber,

        List<GroupResponse> groups,

        Map<String, Object> background,

        List<AnimationResponse> animations,

        TransitionResponse transition,

        List<ComponentResponse> components

) {
}