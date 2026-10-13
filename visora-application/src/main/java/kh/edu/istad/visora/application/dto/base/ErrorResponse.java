package kh.edu.istad.visora.application.dto.base;

import lombok.Builder;

@Builder
public record ErrorResponse<T>(
        String message,
        T detail,
        int status
) {
}
