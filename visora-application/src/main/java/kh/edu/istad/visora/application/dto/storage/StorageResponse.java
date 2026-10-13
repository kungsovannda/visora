package kh.edu.istad.visora.application.dto.storage;

import java.sql.Timestamp;

public record StorageResponse(

        String uuid,

        String originalFileName,

        String fileName,

        String extension,

        String mimeType,

        String  createdBy,

        Timestamp createdAt,

        String updatedBy,

        Timestamp updatedAt

) {
}