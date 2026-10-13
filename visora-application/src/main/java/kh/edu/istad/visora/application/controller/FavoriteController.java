package kh.edu.istad.visora.application.controller;

import kh.edu.istad.visora.application.dto.base.ApiResponse;
import kh.edu.istad.visora.application.dto.favorite.request.CreateFavoriteRequest;
import kh.edu.istad.visora.application.dto.favorite.response.FavoriteResponse;
import kh.edu.istad.visora.application.mapper.FavoriteApplicationMapper;
import kh.edu.istad.visora.domain.dto.request.FavoriteFilter;
import kh.edu.istad.visora.domain.dto.response.Page;
import kh.edu.istad.visora.domain.entity.enums.MainType;
import kh.edu.istad.visora.domain.enums.FavoriteSort;
import kh.edu.istad.visora.domain.ports.inbound.usecase.favorite.CreateFavoriteUseCase;
import kh.edu.istad.visora.domain.ports.inbound.usecase.favorite.DeleteFavoriteUseCase;
import kh.edu.istad.visora.domain.ports.inbound.usecase.favorite.QueryFavoriteUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/favorites")
public class FavoriteController {

    private final CreateFavoriteUseCase createFavoriteUseCase;
    private final QueryFavoriteUseCase queryFavoriteUseCase;
    private final DeleteFavoriteUseCase deleteFavoriteUseCase;
    private final FavoriteApplicationMapper favoriteApplicationMapper;

    @GetMapping
    public ResponseEntity<ApiResponse<Page<FavoriteResponse>>> getCurrentUserFavorites(
            @RequestParam(defaultValue = "0", required = false) int pageNumber,
            @RequestParam(defaultValue = "25", required = false) int pageSize,
            @RequestParam(required = false) MainType type,
            @RequestParam(required = false) String sort
    ) {
        var filter = new FavoriteFilter(
                pageNumber,
                pageSize,
                type,
                resolveSort(sort)
        );
        var query = queryFavoriteUseCase.getCurrentUserFavorites(filter);
        var page = new Page<>(
                query.contents().stream().map(favoriteApplicationMapper::toResponse).toList(),
                query.pageNumber(),
                query.pageSize(),
                query.totalElements(),
                query.totalPages()
        );
        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse<>(
                page,
                HttpStatus.OK.value(),
                true,
                "Retrieved favorites data"
        ));
    }

    @GetMapping("/{favoriteUuid}")
    public ResponseEntity<ApiResponse<FavoriteResponse>> getFavorite(@PathVariable String favoriteUuid) {
        var favorite = queryFavoriteUseCase.getByUuid(favoriteUuid);
        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse<>(
                favoriteApplicationMapper.toResponse(favorite),
                HttpStatus.OK.value(),
                true,
                "Retrieved favorite data"
        ));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<FavoriteResponse>> createFavorite(
            @RequestBody CreateFavoriteRequest request
    ) {
        var favorite = createFavoriteUseCase.create(request.templateUuid(), request.backdropUuid());
        return ResponseEntity.status(HttpStatus.CREATED).body(new ApiResponse<>(
                favoriteApplicationMapper.toResponse(favorite),
                HttpStatus.CREATED.value(),
                true,
                "Favorite added successfully"
        ));
    }

    @DeleteMapping("/{favoriteUuid}")
    public ResponseEntity<ApiResponse<String>> deleteFavorite(@PathVariable String favoriteUuid) {
        deleteFavoriteUseCase.delete(favoriteUuid);
        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse<>(
                "",
                HttpStatus.OK.value(),
                true,
                "Favorite removed successfully"
        ));
    }

    private FavoriteSort resolveSort(String sort) {
        if (sort == null || sort.isBlank()) {
            return FavoriteSort.NEWEST;
        }
        try {
            return FavoriteSort.valueOf(sort.toUpperCase());
        } catch (IllegalArgumentException e) {
            return FavoriteSort.NEWEST;
        }
    }

}
