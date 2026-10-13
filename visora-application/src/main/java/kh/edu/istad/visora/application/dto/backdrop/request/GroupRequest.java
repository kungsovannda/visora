package kh.edu.istad.visora.application.dto.backdrop.request;

public record GroupRequest(

        String uuid,

        String name,

        boolean visible,

        boolean locked

) {
}