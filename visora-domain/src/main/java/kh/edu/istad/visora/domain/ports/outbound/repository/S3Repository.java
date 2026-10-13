package kh.edu.istad.visora.domain.ports.outbound.repository;

import kh.edu.istad.visora.domain.dto.request.S3UploadRequest;

import java.io.InputStream;

public interface S3Repository {

    void upload(S3UploadRequest request);

    InputStream get(String path);

}
