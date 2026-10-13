package kh.edu.istad.visora.application.controller;

import jakarta.validation.Valid;
import kh.edu.istad.visora.application.dto.backdrop.request.DuplicateBackdropRequest;
import kh.edu.istad.visora.application.dto.base.ApiResponse;
import kh.edu.istad.visora.application.dto.template.request.CreateTemplateRequest;
import kh.edu.istad.visora.application.dto.template.request.UpdateTemplateRequest;
import kh.edu.istad.visora.application.dto.template.response.TemplateResponse;
import kh.edu.istad.visora.application.dto.template.response.TemplateSummary;
import kh.edu.istad.visora.application.mapper.BackdropApplicationMapper;
import kh.edu.istad.visora.domain.dto.request.TemplateFilter;
import kh.edu.istad.visora.domain.dto.response.Page;
import kh.edu.istad.visora.domain.entity.Backdrop;
import kh.edu.istad.visora.domain.enums.BackdropSort;
import kh.edu.istad.visora.domain.enums.TemplateStatus;
import kh.edu.istad.visora.domain.ports.inbound.usecase.template.CreateTemplateUseCase;
import kh.edu.istad.visora.domain.ports.inbound.usecase.template.DeleteTemplateUseCase;
import kh.edu.istad.visora.domain.ports.inbound.usecase.template.DuplicateTemplateUseCase;
import kh.edu.istad.visora.domain.ports.inbound.usecase.template.QueryTemplateUseCase;
import kh.edu.istad.visora.domain.ports.inbound.usecase.template.ReviewTemplateUseCase;
import kh.edu.istad.visora.domain.ports.inbound.usecase.template.UpdateTemplateUseCase;
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
@RequestMapping("/api/v1/templates")
public class TemplateController {

    private final QueryTemplateUseCase queryTemplateUseCase;
    private final CreateTemplateUseCase createTemplateUseCase;
    private final UpdateTemplateUseCase updateTemplateUseCase;
    private final DeleteTemplateUseCase deleteTemplateUseCase;
    private final DuplicateTemplateUseCase duplicateTemplateUseCase;
    private final ReviewTemplateUseCase reviewTemplateUseCase;
    private final BackdropApplicationMapper backdropApplicationMapper;

    @GetMapping("/me")
    public ResponseEntity<ApiResponse<Page<TemplateSummary>>> getCurrentUserTemplates(
            @RequestParam(required = false) String search,
            @RequestParam(required = false) Backdrop.Status status,
            @RequestParam(required = false) TemplateStatus templateStatus,
            @RequestParam(required = false) Boolean hasTimer,
            @RequestParam(required = false) String sort,
            @RequestParam(defaultValue = "0") int pageNumber,
            @RequestParam(defaultValue = "25") int pageSize
    ) {
        var filter = new TemplateFilter(
                pageNumber,
                pageSize,
                search,
                status,
                templateStatus,
                hasTimer,
                resolveSort(sort)
        );
        var query = queryTemplateUseCase.getCurrentUserTemplates(filter);
        var result = new Page<>(
                query.contents().stream().map(backdropApplicationMapper::toTemplateSummary).toList(),
                query.pageNumber(),
                query.pageSize(),
                query.totalElements(),
                query.totalPages()
        );
        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse<>(
                result,
                HttpStatus.OK.value(),
                true,
                "Retrieved templates data"
        ));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<Page<TemplateSummary>>> getAllTemplates(
            @RequestParam(required = false) String search,
            @RequestParam(required = false) Backdrop.Status status,
            @RequestParam(required = false) TemplateStatus templateStatus,
            @RequestParam(required = false) Boolean hasTimer,
            @RequestParam(required = false) String sort,
            @RequestParam(defaultValue = "0") int pageNumber,
            @RequestParam(defaultValue = "25") int pageSize
    ) {
        var filter = new TemplateFilter(
                pageNumber,
                pageSize,
                search,
                status,
                templateStatus,
                hasTimer,
                resolveSort(sort)
        );
        var query = queryTemplateUseCase.getAllTemplates(filter);
        var result = new Page<>(
                query.contents().stream().map(backdropApplicationMapper::toTemplateSummary).toList(),
                query.pageNumber(),
                query.pageSize(),
                query.totalElements(),
                query.totalPages()
        );
        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse<>(
                result,
                HttpStatus.OK.value(),
                true,
                "Retrieved all templates data"
        ));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<TemplateSummary>> createTemplate(
            @Valid @RequestBody CreateTemplateRequest request
    ) {
        var template = createTemplateUseCase.create(
                backdropApplicationMapper.toDomain(request),
                request.categoryUuid()
        );
        return ResponseEntity.status(HttpStatus.CREATED).body(new ApiResponse<>(
                backdropApplicationMapper.toTemplateSummary(template),
                HttpStatus.CREATED.value(),
                true,
                "Template created successfully"
        ));
    }

    @GetMapping("/{templateUuid}")
    public ResponseEntity<ApiResponse<TemplateResponse>> getTemplate(@PathVariable String templateUuid) {
        var template = queryTemplateUseCase.getByUuid(templateUuid);
        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse<>(
                backdropApplicationMapper.toTemplateResponse(template),
                HttpStatus.OK.value(),
                true,
                "Retrieved template data"
        ));
    }

    @PatchMapping("/{templateUuid}")
    public ResponseEntity<ApiResponse<TemplateSummary>> updateTemplate(
            @PathVariable String templateUuid,
            @Valid @RequestBody UpdateTemplateRequest request
    ) {
        var template = updateTemplateUseCase.update(
                templateUuid,
                request.categoryUuid(),
                backdropApplicationMapper.toDomain(request)
        );
        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse<>(
                backdropApplicationMapper.toTemplateSummary(template),
                HttpStatus.OK.value(),
                true,
                "Template saved successfully"
        ));
    }

    @DeleteMapping("/{templateUuid}")
    public ResponseEntity<ApiResponse<String>> deleteTemplate(@PathVariable String templateUuid) {
        deleteTemplateUseCase.delete(templateUuid);
        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse<>(
                "",
                HttpStatus.OK.value(),
                true,
                "Template archived successfully"
        ));
    }

    @PostMapping("/{templateUuid}/duplicate")
    public ResponseEntity<ApiResponse<TemplateSummary>> duplicateTemplate(
            @PathVariable String templateUuid,
            @RequestBody(required = false) DuplicateBackdropRequest request
    ) {
        var name = (request == null) ? null : request.name();
        var template = duplicateTemplateUseCase.duplicate(templateUuid, name);
        return ResponseEntity.status(HttpStatus.CREATED).body(new ApiResponse<>(
                backdropApplicationMapper.toTemplateSummary(template),
                HttpStatus.CREATED.value(),
                true,
                "Template duplicated successfully"
        ));
    }

    @PostMapping("/{templateUuid}/approve")
    public ResponseEntity<ApiResponse<TemplateSummary>> approveTemplate(@PathVariable String templateUuid) {
        var template = reviewTemplateUseCase.review(templateUuid, TemplateStatus.APPROVED);
        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse<>(
                backdropApplicationMapper.toTemplateSummary(template),
                HttpStatus.OK.value(),
                true,
                "Template approved successfully"
        ));
    }

    @PostMapping("/{templateUuid}/reject")
    public ResponseEntity<ApiResponse<TemplateSummary>> rejectTemplate(@PathVariable String templateUuid) {
        var template = reviewTemplateUseCase.review(templateUuid, TemplateStatus.REJECTED);
        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse<>(
                backdropApplicationMapper.toTemplateSummary(template),
                HttpStatus.OK.value(),
                true,
                "Template rejected successfully"
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