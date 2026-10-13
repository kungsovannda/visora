package kh.edu.istad.visora.domain.dto.request;

import kh.edu.istad.visora.domain.entity.enums.MainType;
import kh.edu.istad.visora.domain.enums.FavoriteSort;

public record FavoriteFilter(
        int pageNumber,
        int pageSize,
        MainType type,
        FavoriteSort sort
) {
}
