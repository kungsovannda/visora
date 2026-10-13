package kh.edu.istad.visora.domain.dto.request;

import kh.edu.istad.visora.domain.entity.Backdrop;
import kh.edu.istad.visora.domain.enums.BackdropSort;

public record BackdropFilter(
        int pageNumber,
        int pageSize,
        String search,
        Backdrop.Status status,
        Boolean hasTimer,
        BackdropSort sort
) {
}