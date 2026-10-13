package kh.edu.istad.visora.application.dto.backdrop.response;

public record GroupResponse(

        String uuid,

        String name,

        boolean visible,

        boolean locked

) {
}