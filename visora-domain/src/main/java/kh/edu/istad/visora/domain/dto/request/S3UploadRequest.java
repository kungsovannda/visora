package kh.edu.istad.visora.domain.dto.request;

import java.io.InputStream;

public record S3UploadRequest(
        InputStream stream,
        long size,
        String contentType,
        String objectKey
) {
}
