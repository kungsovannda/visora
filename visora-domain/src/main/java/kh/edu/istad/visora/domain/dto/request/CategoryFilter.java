package kh.edu.istad.visora.domain.dto.request;

import kh.edu.istad.visora.domain.enums.CategorySort;

public record CategoryFilter(
        int pageNumber,
        int pageSize,
        String search,
        Boolean isActive,
        CategorySort sort
) {
}