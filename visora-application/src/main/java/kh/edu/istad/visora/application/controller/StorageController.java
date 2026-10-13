package kh.edu.istad.visora.application.controller;

import kh.edu.istad.visora.application.dto.base.ApiResponse;
import kh.edu.istad.visora.application.dto.storage.StorageResponse;
import kh.edu.istad.visora.application.mapper.StorageApplicationMapper;
import kh.edu.istad.visora.domain.dto.response.Page;
import kh.edu.istad.visora.domain.entity.Storage;
import kh.edu.istad.visora.domain.ports.inbound.usecase.storage.QueryStorageUseCase;
import kh.edu.istad.visora.domain.ports.inbound.usecase.storage.UploadStorageUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;

@RestController
@RequiredArgsConstructor
public class StorageController {

    private final UploadStorageUseCase uploadStorageUseCase;
    private final QueryStorageUseCase queryStorageUseCase;
    private final StorageApplicationMapper storageApplicationMapper;

    @PostMapping(value = "/api/v1/storage", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponse<StorageResponse>> upload(@RequestParam("file") MultipartFile file) throws IOException {
        Storage storage = uploadStorageUseCase.upload(
                file.getOriginalFilename(),
                file.getContentType(),
                file.getSize(),
                file.getInputStream()
        );
        return ResponseEntity.status(HttpStatus.CREATED).body(
                new ApiResponse<>(
                        storageApplicationMapper.toResponse(storage),
                        HttpStatus.CREATED.value(),
                        true,
                        "File uploaded successfully"
                )
        );
    }

    @GetMapping("/api/v1/storages")
    public ResponseEntity<ApiResponse<Page<StorageResponse>>> getAllStorages(
            @RequestParam(defaultValue = "0", required = false) int pageNumber,
            @RequestParam(defaultValue = "25", required = false) int pageSize
            ){
        var query = queryStorageUseCase.getAllStorages(pageNumber, pageSize);
        var response = new Page<>(
                query.contents().stream().map(storageApplicationMapper::toResponse).toList(),
                query.pageNumber(),
                query.pageSize(),
                query.totalElements(),
                query.totalPages()
        );
        return ResponseEntity.status(200).body(new ApiResponse<>(response, 200, true, "Retrieve storages data"));
    }

    @GetMapping("/api/v1/storages/{userUuid}")
    public ResponseEntity<ApiResponse<Page<StorageResponse>>> getAllStoragesByUserUuid(
            @RequestParam(defaultValue = "0", required = false) int pageNumber,
            @RequestParam(defaultValue = "25", required = false) int pageSize,
            @PathVariable String userUuid
    ){
        var query = queryStorageUseCase.getAllStoragesByUser(pageNumber, pageSize, userUuid);
        var response = new Page<>(
                query.contents().stream().map(storageApplicationMapper::toResponse).toList(),
                query.pageNumber(),
                query.pageSize(),
                query.totalElements(),
                query.totalPages()
        );
        return ResponseEntity.status(200).body(new ApiResponse<>(response, 200, true, "Retrieve storages data"));
    }

    @GetMapping("/storage/{fileName}")
    public ResponseEntity<byte[]> getFile(@PathVariable String fileName) throws IOException {
        Storage storage = queryStorageUseCase.getByFileName(fileName);
        String mimeType = (storage.getMimeType() == null || storage.getMimeType().isBlank())
                ? MediaType.APPLICATION_OCTET_STREAM_VALUE
                : storage.getMimeType();
        try (InputStream stream = queryStorageUseCase.getContent(storage.getPath())) {
            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType(mimeType))
                    .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + storage.getOriginalFileName() + "\"")
                    .body(stream.readAllBytes());
        }
    }

}