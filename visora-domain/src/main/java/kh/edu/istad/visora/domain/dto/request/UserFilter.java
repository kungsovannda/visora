package kh.edu.istad.visora.domain.dto.request;

import kh.edu.istad.visora.domain.enums.UserSort;

public record UserFilter(
        Integer pageNumber,
        Integer pageSize,
        String search,
        String role,
        UserSort sort
) {
}