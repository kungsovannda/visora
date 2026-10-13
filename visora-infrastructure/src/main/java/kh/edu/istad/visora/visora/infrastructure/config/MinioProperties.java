package kh.edu.istad.visora.visora.infrastructure.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "minio")
public record MinioProperties(
        String endpoint,
        String bucket,
        String accessKey,
        String secretKey

) {
}
