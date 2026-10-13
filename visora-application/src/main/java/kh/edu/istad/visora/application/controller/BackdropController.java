package kh.edu.istad.visora.application.controller;

import jakarta.validation.Valid;
import kh.edu.istad.visora.application.dto.backdrop.request.CreateBackdropRequest;
import kh.edu.istad.visora.application.dto.backdrop.request.DuplicateBackdropRequest;
import kh.edu.istad.visora.application.dto.backdrop.request.UpdateBackdropRequest;
import kh.edu.istad.visora.application.dto.backdrop.response.BackdropResponse;
import kh.edu.istad.visora.application.dto.backdrop.response.BackdropSummary;
import kh.edu.istad.visora.application.dto.base.ApiResponse;
import kh.edu.istad.visora.application.dto.template.request.SubmitTemplateRequest;
import kh.edu.istad.visora.application.dto.template.response.TemplateResponse;
import kh.edu.istad.visora.application.mapper.BackdropApplicationMapper;
import kh.edu.istad.visora.domain.dto.request.BackdropFilter;
import kh.edu.istad.visora.domain.dto.response.Page;
import kh.edu.istad.visora.domain.entity.Backdrop;
import kh.edu.istad.visora.domain.enums.BackdropSort;
import kh.edu.istad.visora.domain.ports.inbound.usecase.backdrop.CreateBackdropUseCase;
import kh.edu.istad.visora.domain.ports.inbound.usecase.backdrop.DeleteBackdropUseCase;
import kh.edu.istad.visora.domain.ports.inbound.usecase.backdrop.DuplicateBackdropUseCase;
import kh.edu.istad.visora.domain.ports.inbound.usecase.backdrop.QueryBackdropUseCase;
import kh.edu.istad.visora.domain.ports.inbound.usecase.backdrop.UpdateBackdropUseCase;
import kh.edu.istad.visora.domain.ports.inbound.usecase.template.SubmitTemplateUseCase;
import kh.edu.istad.visora.domain.ports.inbound.usecase.template.WithdrawTemplateUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/backdrops")
public class BackdropController {

    private final QueryBackdropUseCase queryBackdropUseCase;
    private final CreateBackdropUseCase createBackdropUseCase;
    private final UpdateBackdropUseCase updateBackdropUseCase;
    private final DeleteBackdropUseCase deleteBackdropUseCase;
    private final DuplicateBackdropUseCase duplicateBackdropUseCase;
    private final SubmitTemplateUseCase submitTemplateUseCase;
    private final WithdrawTemplateUseCase withdrawTemplateUseCase;
    private final BackdropApplicationMapper backdropApplicationMapper;

    @GetMapping
    public ResponseEntity<ApiResponse<Page<BackdropSummary>>> getCurrentUserBackdrops(
            @RequestParam(required = false) String search,
            @RequestParam(required = false) Backdrop.Status status,
            @RequestParam(required = false) Boolean hasTimer,
            @RequestParam(required = false) String sort,
            @RequestParam(defaultValue = "0") int pageNumber,
            @RequestParam(defaultValue = "25") int pageSize
    ) {
        var filter = new BackdropFilter(
                pageNumber,
                pageSize,
                search,
                status,
                hasTimer,
                resolveSort(sort)
        );
        var query = queryBackdropUseCase.getCurrentUserBackdrops(filter);
        var result = new Page<>(
                query.contents().stream().map(backdropApplicationMapper::toSummary).toList(),
                query.pageNumber(),
                query.pageSize(),
                query.totalElements(),
                query.totalPages()
        );
        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse<>(
                result,
                HttpStatus.OK.value(),
                true,
                "Retrieved backdrops data"
        ));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<BackdropSummary>> createBackdrop(
            @Valid @RequestBody CreateBackdropRequest request
    ) {
        var backdrop = createBackdropUseCase.create(backdropApplicationMapper.toDomain(request));
        return ResponseEntity.status(HttpStatus.CREATED).body(new ApiResponse<>(
                backdropApplicationMapper.toSummary(backdrop),
                HttpStatus.CREATED.value(),
                true,
                "Backdrop created successfully"
        ));
    }

    @GetMapping("/{backdropUuid}")
    public ResponseEntity<ApiResponse<BackdropResponse>> getBackdrop(@PathVariable String backdropUuid) {
        var backdrop = queryBackdropUseCase.getByUuid(backdropUuid);
        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse<>(
                backdropApplicationMapper.toResponse(backdrop),
                HttpStatus.OK.value(),
                true,
                "Retrieved backdrop data"
        ));
    }

    @PatchMapping("/{backdropUuid}")
    public ResponseEntity<ApiResponse<BackdropSummary>> updateBackdrop(
            @PathVariable String backdropUuid,
            @Valid @RequestBody UpdateBackdropRequest request
    ) {
        var backdrop = updateBackdropUseCase.update(
                backdropUuid,
                backdropApplicationMapper.toDomain(request)
        );
        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse<>(
                backdropApplicationMapper.toSummary(backdrop),
                HttpStatus.OK.value(),
                true,
                "Backdrop saved successfully"
        ));
    }

    @DeleteMapping("/{backdropUuid}")
    public ResponseEntity<ApiResponse<String>> deleteBackdrop(@PathVariable String backdropUuid) {
        deleteBackdropUseCase.delete(backdropUuid);
        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse<>(
                "",
                HttpStatus.OK.value(),
                true,
                "Backdrop archived successfully"
        ));
    }

    @PostMapping("/{backdropUuid}/duplicate")
    public ResponseEntity<ApiResponse<BackdropSummary>> duplicateBackdrop(
            @PathVariable String backdropUuid,
            @RequestBody(required = false) DuplicateBackdropRequest request
    ) {
        var name = (request == null) ? null : request.name();
        var backdrop = duplicateBackdropUseCase.duplicate(backdropUuid, name);
        return ResponseEntity.status(HttpStatus.CREATED).body(new ApiResponse<>(
                backdropApplicationMapper.toSummary(backdrop),
                HttpStatus.CREATED.value(),
                true,
                "Backdrop duplicated successfully"
        ));
    }

    @PostMapping("/{backdropUuid}/templates")
    public ResponseEntity<ApiResponse<TemplateResponse>> submitTemplate(
            @PathVariable String backdropUuid,
            @RequestBody SubmitTemplateRequest request
    ) {
        var template = submitTemplateUseCase.submit(
                backdropUuid,
                request.categoryUuid(),
                backdropApplicationMapper.toDomain(request)
        );
        return ResponseEntity.status(HttpStatus.CREATED).body(new ApiResponse<>(
                backdropApplicationMapper.toTemplateResponse(template),
                HttpStatus.CREATED.value(),
                true,
                "Template submitted for publication review"
        ));
    }

    @DeleteMapping("/{backdropUuid}/templates")
    public ResponseEntity<ApiResponse<String>> withdrawTemplate(@PathVariable String backdropUuid) {
        withdrawTemplateUseCase.withdraw(backdropUuid);
        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse<>(
                "",
                HttpStatus.OK.value(),
                true,
                "Template submission withdrawn successfully"
        ));
    }

    private BackdropSort resolveSort(String sort) {

        if (sort == null || sort.isBlank()) {
            return BackdropSort.LAST_UPDATED;
        }
        try {
            return BackdropSort.valueOf(sort.toUpperCase());
        } catch (IllegalArgumentException e) {
            return BackdropSort.LAST_UPDATED;
        }
    }

}