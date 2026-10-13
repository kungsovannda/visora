package kh.edu.istad.visora.domain.ports.inbound.usecase.storage;

import kh.edu.istad.visora.domain.dto.request.S3UploadRequest;
import kh.edu.istad.visora.domain.entity.Storage;
import kh.edu.istad.visora.domain.ports.CurrentUser;
import kh.edu.istad.visora.domain.ports.outbound.repository.S3Repository;
import kh.edu.istad.visora.domain.ports.outbound.repository.StorageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UploadStorageUseCase {

    private static final String STORAGE_PATH = "uploads";

    private final StorageRepository storageRepository;
    private final S3Repository s3Repository;
    private final CurrentUser currentUser;

    public Storage upload(String originalFileName, String contentType, long size, InputStream stream) {
        String extension = resolveExtension(originalFileName);
        String fileName = UUID.randomUUID().toString().replace("-", "") + (extension.isBlank() ? "" : "." + extension);
        String path = STORAGE_PATH + "/" + fileName;

        Storage storage = new Storage();
        storage.setUuid(UUID.randomUUID().toString());
        storage.setOwner(currentUser.get());
        storage.setOriginalFileName(originalFileName);
        storage.setFileName(fileName);
        storage.setExtension(extension);
        storage.setMimeType(contentType);
        storage.setPath(path);

        s3Repository.upload(new S3UploadRequest(stream, size, contentType, path));
        return storageRepository.save(storage);
    }

    private String resolveExtension(String originalFileName) {
        if (originalFileName == null || originalFileName.isBlank()) {
            return "";
        }
        int dot = originalFileName.lastIndexOf('.');
        return dot == -1 ? "" : originalFileName.substring(dot + 1);
    }

}