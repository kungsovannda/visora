package kh.edu.istad.visora.application.dto.backdrop.request;

public record TransitionRequest(

        String preset,

        double durationMs,

        double delayMs

) {
}