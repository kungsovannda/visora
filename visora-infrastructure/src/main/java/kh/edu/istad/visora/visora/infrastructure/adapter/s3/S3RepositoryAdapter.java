package kh.edu.istad.visora.visora.infrastructure.adapter.s3;

import io.minio.BucketExistsArgs;
import io.minio.GetObjectArgs;
import io.minio.MakeBucketArgs;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import io.minio.errors.MinioException;
import kh.edu.istad.visora.domain.dto.request.S3UploadRequest;
import kh.edu.istad.visora.domain.ports.outbound.repository.S3Repository;
import kh.edu.istad.visora.visora.infrastructure.config.MinioProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.io.InputStream;

@Repository
@RequiredArgsConstructor
public class S3RepositoryAdapter implements S3Repository {

    private final MinioClient minioClient;
    private final MinioProperties minioProperties;

    @Override
    public void upload(S3UploadRequest request) {
        ensureBucketExists();
        try {
            minioClient.putObject(PutObjectArgs.builder()
                            .bucket(minioProperties.bucket())
                            .stream(request.stream(), request.size(), -1L)
                            .object(request.objectKey()).build()
            );

        } catch (MinioException e) {
            throw new RuntimeException("Failed to upload file to storage", e);
        }
    }

    @Override
    public InputStream get(String objectKey) {
        try {
            return minioClient.getObject(
                    GetObjectArgs.builder()
                            .bucket(minioProperties.bucket())
                            .object(objectKey)
                            .build()
            );

        } catch (MinioException e) {
            throw new RuntimeException("Failed to retrieve file from storage", e);
        }
    }

    private void ensureBucketExists() {
        try {
            boolean exists = minioClient.bucketExists(
                    BucketExistsArgs.builder()
                            .bucket(minioProperties.bucket())
                            .build()
            );
            if (!exists) {
                minioClient.makeBucket(
                        MakeBucketArgs.builder()
                                .bucket(minioProperties.bucket())
                                .build()
                );
            }
        } catch (MinioException e) {
            throw new RuntimeException("Failed to initialize storage bucket", e);
        }
    }

}