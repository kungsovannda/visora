package kh.edu.istad.visora.application.dto.backdrop.request;

public record AnimationRequest(

        String id,

        String elementId,

        String kind,

        String preset,

        String trigger,

        double delayMs,

        double durationMs

) {
}