package kh.edu.istad.visora.application.dto.backdrop.response;

public record TransitionResponse(

        String preset,

        double durationMs,

        double delayMs

) {
}