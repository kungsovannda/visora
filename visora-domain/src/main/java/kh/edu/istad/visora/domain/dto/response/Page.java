package kh.edu.istad.visora.domain.dto.response;

import java.util.List;

public record Page<T>(
        List<T> contents,
        int pageNumber,
        int pageSize,
        long totalElements,
        int totalPages
) {
}
