package kh.edu.istad.visora.domain.dto.request;

import kh.edu.istad.visora.domain.entity.Backdrop;
import kh.edu.istad.visora.domain.enums.BackdropSort;
import kh.edu.istad.visora.domain.enums.TemplateStatus;

public record TemplateFilter(
        int pageNumber,
        int pageSize,
        String search,
        Backdrop.Status status,
        TemplateStatus templateStatus,
        Boolean hasTimer,
        BackdropSort sort
) {
}