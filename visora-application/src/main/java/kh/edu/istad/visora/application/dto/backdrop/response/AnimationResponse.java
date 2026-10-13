package kh.edu.istad.visora.application.dto.backdrop.response;

public record AnimationResponse(

        String id,

        String elementId,

        String kind,

        String preset,

        String trigger,

        double delayMs,

        double durationMs

) {
}