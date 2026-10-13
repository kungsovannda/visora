package kh.edu.istad.visora.application.controller;

import jakarta.validation.Valid;
import kh.edu.istad.visora.application.dto.base.ApiResponse;
import kh.edu.istad.visora.application.dto.category.CategoryResponse;
import kh.edu.istad.visora.application.dto.category.CreateCategoryRequest;
import kh.edu.istad.visora.application.dto.category.UpdateCategoryRequest;
import kh.edu.istad.visora.application.mapper.CategoryApplicationMapper;
import kh.edu.istad.visora.domain.dto.request.CategoryFilter;
import kh.edu.istad.visora.domain.dto.response.Page;
import kh.edu.istad.visora.domain.enums.CategorySort;
import kh.edu.istad.visora.domain.ports.inbound.usecase.category.CreateCategoryUseCase;
import kh.edu.istad.visora.domain.ports.inbound.usecase.category.DeleteCategoryUseCase;
import kh.edu.istad.visora.domain.ports.inbound.usecase.category.QueryCategoryUseCase;
import kh.edu.istad.visora.domain.ports.inbound.usecase.category.UpdateCategoryUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/categories")
public class CategoryController {

    private final CreateCategoryUseCase createCategoryUseCase;
    private final QueryCategoryUseCase queryCategoryUseCase;
    private final UpdateCategoryUseCase updateCategoryUseCase;
    private final DeleteCategoryUseCase deleteCategoryUseCase;
    private final CategoryApplicationMapper categoryApplicationMapper;

    @ResponseStatus(HttpStatus.OK)
    @GetMapping
    public ResponseEntity<ApiResponse<Page<CategoryResponse>>> getAllCategories(
            @RequestParam(defaultValue = "0", required = false) int pageNumber,
            @RequestParam(defaultValue = "25", required = false) int pageSize,
            @RequestParam(required = false) String search,
            @RequestParam(required = false) Boolean isActive,
            @RequestParam(required = false) String sort
    ) {
        var filter = new CategoryFilter(
                pageNumber,
                pageSize,
                search,
                isActive,
                resolveSort(sort)
        );
        var query = queryCategoryUseCase.getAllCategory(filter);
        var page = new Page<>(
                query.contents().stream().map(categoryApplicationMapper::toResponse).toList(),
                query.pageNumber(),
                query.pageSize(),
                query.totalElements(),
                query.totalPages()
        );
        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse<>(
                page,
                HttpStatus.OK.value(),
                true,
                "Retrieved categories data"
        ));
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/{uuid}")
    public ResponseEntity<ApiResponse<CategoryResponse>> getCategoryByUuid(@PathVariable String uuid) {
        var query = queryCategoryUseCase.getByUuid(uuid);
        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse<>(
                categoryApplicationMapper.toResponse(query),
                HttpStatus.OK.value(),
                true,
                "Retrieved category data"
        ));
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public ResponseEntity<ApiResponse<CategoryResponse>> createCategory(
            @Valid @RequestBody CreateCategoryRequest request
    ) {
        var create = createCategoryUseCase.create(categoryApplicationMapper.toDomain(request));
        return ResponseEntity.status(HttpStatus.CREATED).body(
                new ApiResponse<>(
                        categoryApplicationMapper.toResponse(create),
                        HttpStatus.CREATED.value(),
                        true,
                        "Category created successfully"
                )
        );
    }

    @ResponseStatus(HttpStatus.OK)
    @PatchMapping("/{uuid}")
    public ResponseEntity<ApiResponse<CategoryResponse>> updateCategory(
            @PathVariable String uuid,
            @Valid @RequestBody UpdateCategoryRequest request
    ) {
        var update = updateCategoryUseCase.update(uuid, categoryApplicationMapper.toDomain(request));
        return ResponseEntity.status(HttpStatus.OK).body(
                new ApiResponse<>(
                        categoryApplicationMapper.toResponse(update),
                        HttpStatus.OK.value(),
                        true,
                        "Category updated successfully"
                )
        );
    }

    @ResponseStatus(HttpStatus.OK)
    @DeleteMapping("/{uuid}")
    public ResponseEntity<ApiResponse<String>> deleteCategory(@PathVariable String uuid) {
        deleteCategoryUseCase.delete(uuid);
        return ResponseEntity.status(HttpStatus.OK).body(
                new ApiResponse<>(
                        "",
                        HttpStatus.OK.value(),
                        true,
                        "Category deleted successfully"
                )
        );
    }

    private CategorySort resolveSort(String sort) {
        if (sort == null || sort.isBlank()) {
            return CategorySort.DISPLAY_ORDER;
        }
        try {
            return CategorySort.valueOf(sort.toUpperCase());
        } catch (IllegalArgumentException e) {
            return CategorySort.DISPLAY_ORDER;
        }
    }

}