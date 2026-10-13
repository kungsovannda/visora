package kh.edu.istad.visora.application.dto.base;

public record ApiResponse<T>(
        T data,
        int status,
        boolean success,
        String message
) {
}
